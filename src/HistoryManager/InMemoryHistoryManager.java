package HistoryManager;

import InMemoryTaskManager.InMemoryTaskManager;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public static List<Task> viewHistory = new ArrayList<>();


    @Override
    public List<Task> getHistory(){
        viewHistory.forEach(task -> System.out.println(task));
        return viewHistory;
    }

    @Override
    public<T extends Task> void add(T task) throws CloneNotSupportedException {

        T copy = (T) task.clone();

        boolean sizeListMoreThen10 = viewHistory.size() > 10 ? true : false;

        switch(sizeListMoreThen10) {
            case true -> {
                viewHistory.remove(viewHistory.get(0));
                viewHistory.add(copy);
            }

            case false -> viewHistory.add(copy);
        }

    }
}
