package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {

    private final LinkedList<Node> viewHistory = new LinkedList<>();
    private final HashMap<Integer, Node> nodes = new HashMap<>();

    public ArrayList<T> getTasks() {
        ArrayList<T> history = new ArrayList<>();
        for (Node node : viewHistory) {
            history.add((T) node.getData());
        }
        return history;
    }

    @Override
    public void removeNode(Node node) {
        viewHistory.remove(node);
    }

    @Override
    public Node linkLast(T task) {
        Node newNode = new Node<>(task);
        viewHistory.addLast(newNode);
        return newNode;
    }

    @Override
    public void add(T task) {
        if (nodes.containsKey(task.getId())) {
            removeNode(nodes.get(task.getId()));
        }
        nodes.put(task.getId(), linkLast(task));
    }

    @Override
    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }
}
