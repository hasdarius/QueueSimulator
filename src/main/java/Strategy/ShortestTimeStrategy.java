package Strategy;

import Client.Client;
import Queue.Queue;

import java.util.List;

public class ShortestTimeStrategy implements Strategy {
    public void addClient(List<Queue> queues, Client client) {
        Queue queue = queues.get(0);
        for (Queue iterator : queues) {
            if (iterator.getWaitingPeriod().intValue() < queue.getWaitingPeriod().intValue())
                queue=iterator;
        }
       queue.addClient(client);
        queue.startThread();
    }
}
