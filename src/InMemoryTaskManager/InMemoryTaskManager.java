package InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    public static int idCount = 0;
    public final HashMap<Integer, Task> taskList = new HashMap<>();
    public final HashMap<Integer, Epic> epicList = new HashMap<>();
    public final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private List<Task> viewHistory = new ArrayList<>();


    public static int getIdCount() {
        return idCount;
    }

    public static void addIdCount() {
        idCount++;
    }

    @Override
    public void addTaskToList(Task task, HashMap hashMap) {
        hashMap.put(task.getId(), task);
    }

    @Override
    public Task createTask(String title, String description) {
        addIdCount();
        Task task = new Task(title, description, getIdCount(), Status.NEW);
        return task;
    }

    @Override
    public Epic createEpic(String title, String description) {
        addIdCount();
        Epic epic = new Epic(title, description, getIdCount(), Status.NEW);
        return epic;
    }

    @Override
    public Subtask createSubtask(String title, String description, int epicId) {
        addIdCount();
        Subtask subtask = new Subtask(title, description, getIdCount(), Status.NEW, epicId);
        return subtask;
    }

    @Override
    public Task findTask(int findId) {
        taskList.putAll(subtaskList);
        taskList.putAll(epicList);
        updateHistory(taskList.get(findId));
        return taskList.get(findId);
    }

    @Override
    public void deleteTaskFromList(int id) {

        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else if (epicList.containsKey(id)) {
            epicList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            int epicIdRemovedSubtask = subtaskList.get(id).getEpicId();
            subtaskList.remove(id);
            updateEpic(epicIdRemovedSubtask);
        } else {
            System.out.println("Данного элемента уже нет в списке");
        }
    }

    @Override
    public ArrayList<Subtask> subtasksThisEpic(int epicId) {
        ArrayList<Subtask> subtasksThisEpic = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksThisEpic.add(subtask);
            }
        }
        return subtasksThisEpic;
    }

    @Override
    public void updateTask(int id) {
        Task task = taskList.get(id);
        Status status = task.getStatus();
        switch (status) {

            case Status.NEW -> task.setStatus(Status.IN_PROGRESS);

            case Status.IN_PROGRESS -> task.setStatus(Status.DONE);
        }
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(int id) {
        if (subtaskList.get(id).getStatus().equals(Status.NEW)) {
            subtaskList.get(id).setSubtaskStatus(Status.IN_PROGRESS);
        } else if (subtaskList.get(id).getStatus().equals(Status.IN_PROGRESS)) {
            subtaskList.get(id).setSubtaskStatus(Status.DONE);
        }
        subtaskList.put(subtaskList.get(id).getId(), subtaskList.get(id));
        updateEpic(subtaskList.get(id).getEpicId());
    }

    @Override
    public void updateEpic(int id) {
        int subtasksInDone = 0;
        int subtasksInProgress = 0;
        Epic epic = epicList.get(id);
        for (Subtask subtask : subtasksThisEpic(id)) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                subtasksInProgress++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                subtasksInDone++;
            }
        }

        if (subtasksInProgress > 0) {
            epic.setStatus(Status.IN_PROGRESS);
            epicList.put(epic.getId(), epic);
        }

        if (subtasksInDone == subtasksThisEpic(id).size()) {
            epic.setStatus(Status.DONE);
            epicList.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteSubtasksThisEpic(int epicId) {
        subtaskList.values().removeAll(subtasksThisEpic(epicId));
        epicList.get(epicId).setStatus(Status.DONE);
        updateEpic(epicId);
    }

    @Override
    public List<Task> getHistory(){
        System.out.println(viewHistory);
        return viewHistory;
    }

    @Override
    public void updateHistory(Task task) {
        boolean sizeListMoreThen10 = viewHistory.size() > 10 ? true : false;

        switch(sizeListMoreThen10) {
            case true -> {
                viewHistory.remove(viewHistory.get(0));
                viewHistory.add(task);
            }

            case false -> viewHistory.add(task);
        }

        }
    }



