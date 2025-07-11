package manager;

import tasks.Task;

import java.util.Objects;

public class Node <T extends Task> {
    public T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Node<?> node = (Node<?>) object;
        return Objects.equals(data, node.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
