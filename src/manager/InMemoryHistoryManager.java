package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> viewHistory = new ArrayList<>();

    public List<Task> getViewHistory() {
        return viewHistory;
    }

    @Override
    public <T extends Task> void add(T task) throws CloneNotSupportedException {

        T copy = (T) task.clone();

        if (viewHistory.size() >= 10) {
            viewHistory.remove(viewHistory.get(0));
        }
        viewHistory.add(copy);
    }
}
