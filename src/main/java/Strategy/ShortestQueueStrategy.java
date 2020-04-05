package Strategy;

import Client.Client;
import Queue.Queue;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {
    public void addClient(List<Queue> queues, Client client) {
        Queue queue = queues.get(0);
        for (Queue iterator : queues) {
            if (iterator.getNrOfWaitingClients().intValue() < queue.getNrOfWaitingClients().intValue())
                queue=iterator;
        }
        queue.addClient(client);
        queue.startThread();

    }
}
