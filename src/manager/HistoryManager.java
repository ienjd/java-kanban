package manager;

import java.util.ArrayList;
import java.util.HashMap;

public interface HistoryManager<T> {

    ArrayList<T> getTasks();

    void removeNode(Node node);

    Node linkLast(T task);

    void add(T task);

    HashMap<Integer, Node> getNodes();
}
