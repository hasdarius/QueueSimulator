package Queue;

import Client.Client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Queue implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private AtomicInteger nrOfWaitingClients;
    private AtomicInteger nrOfTotalClients;
    private AtomicInteger nrOfTotalTime;
    private AtomicBoolean isRunning = new AtomicBoolean(false);


    public Queue() {
        clients = new LinkedBlockingQueue<Client>();
        waitingPeriod = new AtomicInteger(0);
        nrOfWaitingClients = new AtomicInteger(0);
        nrOfTotalClients = new AtomicInteger(0);
        nrOfTotalTime = new AtomicInteger(0);
    }


    public BlockingQueue<Client> getClients() {
        return clients;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public AtomicInteger getNrOfWaitingClients() {
        return nrOfWaitingClients;
    }

    public void addClient(Client client) {
        client.setWaitingTime(this.waitingPeriod.intValue()); //compute waiting time for client
        client.setTotalWaitingTime(client.gettService() + client.getWaitingTime());
        clients.add(client); //add client to queue
        waitingPeriod.addAndGet(client.gettService()); //add to waiting period the new client's service time
        nrOfWaitingClients.addAndGet(1);
    }

    public AtomicInteger getNrOfTotalClients() {
        return nrOfTotalClients;
    }

    public AtomicInteger getNrOfTotalTime() {
        return nrOfTotalTime;
    }

    public void run() {
        isRunning.set(true);
        while (isRunning.get()) {
            try {
                if (!clients.isEmpty()) {
                    Thread.sleep(990);
                    waitingPeriod.decrementAndGet();
                    clients.peek().settService(clients.peek().gettService() - 1);
                    if (clients.peek().gettService() == 0) {
                        nrOfTotalClients.incrementAndGet();
                        nrOfTotalTime.addAndGet(clients.peek().getTotalWaitingTime());
                        nrOfWaitingClients.decrementAndGet();
                        clients.take();
                    }
                } else {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopThread() {
        isRunning.set(false);
    }

    public void startThread() {
        if (Thread.currentThread().isInterrupted() && !clients.isEmpty())
            Thread.currentThread().start();
        isRunning.set(true);
    }
}

