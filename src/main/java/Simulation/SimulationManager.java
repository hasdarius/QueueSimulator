package Simulation;

import Client.Client;
import Schedule.Scheduler;
import Strategy.SelectionPolicy;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManager implements Runnable {
    public Integer timeLimit;
    public Integer minServiceTime;
    public Integer maxServiceTime;
    public Integer minArrivalTime;
    public Integer maxArrivalTime;
    public Integer numberOfClients;
    public Integer numberOfQueues;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private Scheduler scheduler;
    private String fileToWriteInto;
    private List<Client> waitingClients = new CopyOnWriteArrayList<Client>();

    public SimulationManager(String[] argument) {
        readFile(argument[0]);
        try {
            blankFile(argument[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileToWriteInto = argument[1];
        generateRandomClients();
        scheduler = new Scheduler(numberOfQueues, selectionPolicy);


    }

    public void blankFile(String argument) throws IOException {
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(argument, false));
        myWriter.write("");
        myWriter.close();
    }

    public void generateRandomClients() {
        for (Integer iterator = 0; iterator < numberOfClients; iterator++) {
            Client newClient = new Client(iterator + 1, new Random().nextInt(maxArrivalTime + 1) + minArrivalTime, new Random().nextInt(maxServiceTime + 1) + minServiceTime);
            waitingClients.add(newClient);
        }
        waitingClients.sort(Comparator.comparing(Client::gettArrival).thenComparing(Client::gettService));

    }

    public void toPrint(int currentTime) throws IOException {
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(fileToWriteInto, true));
        myWriter.write('\n');
        myWriter.write("Time " + currentTime);
        myWriter.write('\n');
        myWriter.write("Waiting clients: ");
        for (Client iteratorClients : waitingClients) {
            myWriter.write(iteratorClients.toPrint() + "; ");
        }
        myWriter.write('\n');
        for (int iteratorQueues = 0; iteratorQueues < numberOfQueues; iteratorQueues++) {
            myWriter.write("Queue number " + (iteratorQueues + 1) + ":");
            if (scheduler.getQueues().get(iteratorQueues).getClients().isEmpty()) {
                myWriter.write(" closed \n");
            } else {
                for (Client clients : scheduler.getQueues().get(iteratorQueues).getClients()) {
                    myWriter.write(clients.toPrint() + "; ");
                }
                myWriter.write('\n');
            }
        }
        myWriter.write('\n');
        myWriter.close();
    }

    public void computeAverageTime() throws IOException {
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(fileToWriteInto, true));
        Integer nrOfTotalClients = 0;
        Integer nrOfTotalTime = 0;
        for (int iteratorQueues = 0; iteratorQueues < numberOfQueues; iteratorQueues++) {
            nrOfTotalClients += scheduler.getQueues().get(iteratorQueues).getNrOfTotalClients().intValue();
            nrOfTotalTime += scheduler.getQueues().get(iteratorQueues).getNrOfTotalTime().intValue();

        }
        Float averageWaitingTime = nrOfTotalTime.floatValue() / nrOfTotalClients.floatValue();
        myWriter.write('\n');
        myWriter.write("Average waiting time: " + averageWaitingTime);
        myWriter.close();

    }

    public void stopAllThreads() {
        for (int iteratorQueues = 0; iteratorQueues < numberOfQueues; iteratorQueues++) {
            scheduler.getQueues().get(iteratorQueues).stopThread();
        }
    }

    @Override
    public void run() {
        Integer currentTime = 0;
        boolean ok = true;
        while (currentTime < timeLimit && ok == true) {
            for (Client client : waitingClients) {
                if (client.gettArrival() == currentTime) {
                    scheduler.dispatchClient(client);
                    waitingClients.remove(client);
                }
            }
            if (waitingClients.isEmpty())
                ok = false;
            for (int iterator = 0; iterator < numberOfQueues; iterator++) {
                if (!scheduler.getQueues().get(iterator).getClients().isEmpty()) {
                    ok = true;
                }
            }
            if (ok == true) {
                try {
                    toPrint(currentTime);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopAllThreads();
        try {
            computeAverageTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(String file) {
        File newFile = new File(file);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(newFile));
            numberOfClients = Integer.parseInt(reader.readLine());
            numberOfQueues = Integer.parseInt(reader.readLine());
            timeLimit = Integer.parseInt(reader.readLine());
            String line;
            line = reader.readLine();
            minArrivalTime = Integer.parseInt(line.substring(0, line.indexOf(",")));
            maxArrivalTime = Integer.parseInt(line.substring(line.indexOf(",") + 1));
            line = reader.readLine();
            minServiceTime = Integer.parseInt(line.substring(0, line.indexOf(",")));
            maxServiceTime = Integer.parseInt(line.substring(line.indexOf(",") + 1));
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        if(args.length==2) {
            SimulationManager simulationManager = new SimulationManager(args);
            Thread thread = new Thread(simulationManager);
            thread.start();
        } else{
            System.out.println("Please introduce two arguments. One for the input file and one for the output one.");
        }
    }


}
