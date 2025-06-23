package InMemoryTaskManager;

import HistoryManager.HistoryManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new TaskManager() {
            @Override
            public void addTaskToList(Task task, HashMap hashMap) {

            }

            @Override
            public Task createTask(String title, String description) {
                return null;
            }

            @Override
            public Epic createEpic(String title, String description) {
                return null;
            }

            @Override
            public Subtask createSubtask(String title, String description, int epicId) {
                return null;
            }

            @Override
            public Object findTask(int findId) {
                return null;
            }

            @Override
            public void deleteTaskFromList(int id) {

            }

            @Override
            public ArrayList<Subtask> subtasksThisEpic(int epicId) {
                return null;
            }

            @Override
            public void updateTask(int id) {

            }

            @Override
            public void updateSubtask(int id) {

            }

            @Override
            public void updateEpic(int id) {

            }

            @Override
            public void deleteSubtasksThisEpic(int epicId) {

            }
        };
        return taskManager;

    }
    public static HistoryManager getDefaultHistory(){
        HistoryManager historyManager = new HistoryManager() {
            @Override
            public void add(Task task) {

            }

            @Override
            public List<Task> getHistory() {
                return List.of();
            }
        };
        return historyManager;
    }
}

