package Strategy;

import Client.Client;
import Queue.Queue;

import java.util.List;

public interface Strategy {
    public void addClient(List<Queue> queues, Client client);
}
