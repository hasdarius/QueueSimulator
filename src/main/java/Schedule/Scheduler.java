package Schedule;

import Client.Client;
import Queue.Queue;
import Strategy.*;

import java.util.LinkedList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues = new LinkedList<Queue>();
    private Strategy strategy;

    public Scheduler(Integer numberOfQueues, SelectionPolicy policy) {
        for (Integer iterator = 0; iterator < numberOfQueues; iterator++) {
            Queue queue = new Queue();
            queues.add(queue);
            Thread newThread = new Thread(queue);
            newThread.start();
        }
        defineStrategy(policy);
    }

    public void defineStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ShortestQueueStrategy();
        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ShortestTimeStrategy();
    }

    public void dispatchClient(Client client) {
        strategy.addClient(queues, client);
    }

    public List<Queue> getQueues() {
        return queues;
    }

}
