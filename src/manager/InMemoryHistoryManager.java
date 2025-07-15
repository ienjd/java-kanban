package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final LinkedList<Node> viewHistory = new LinkedList<>();
    private final HashMap<Integer, Node> nodes = new HashMap<>();

    @Override
    public List<T> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
    }

    @Override
    public void add(T task) {
        if (nodes.containsKey(task.getId())) {
            remove(task.getId());
        }
        nodes.put(task.getId(), linkLast(task));
    }

    private List<T> getTasks() {
        List<T> history = new ArrayList<>();
        for (Node node : viewHistory) {
            history.add((T) node.getData());
        }
        return history;
    }

    private void removeNode(Node node) {
        viewHistory.remove(node);
    }

    private Node linkLast(T task) {
        Node newNode = new Node<>(task);
        viewHistory.addLast(newNode);
        return newNode;
    }

    private HashMap<Integer, Node> getNodes() {
        return nodes;
    }
}

