package manager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    public static int idCount = 0;
    public final HashMap<Integer, Task> taskList = new HashMap<>();
    public final HashMap<Integer, Epic> epicList = new HashMap<>();
    public final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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
        Subtask subtask = new Subtask(title, description, getIdCount(), Status.NEW, epicId = epicId == getIdCount() ?
                0 : epicId);
        return subtask;
    }

    @Override
    public Object findTask(int findId) throws CloneNotSupportedException {
        Object object = null;
        if (taskList.containsKey(findId)) {
            object = taskList.get(findId);
        } else if (epicList.containsKey(findId)) {
            object = epicList.get(findId);
        } else if (subtaskList.containsKey(findId)) {
            object = subtaskList.get(findId);
        }
        inMemoryHistoryManager.add((Task)object);
        return object;
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
            updateEpic(epicList.get(epicIdRemovedSubtask));
        } else {
            System.out.println("Данного элемента уже нет в списке");
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtasksThisEpic = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksThisEpic.add(subtask);
            }
        }
        return subtasksThisEpic;
    }

    @Override
    public void updateTask(Task task) {
        Status currentStatus = task.getStatus();
        Task newTask = createTask(task.getTitle(), task.getDescription());
        idCount--;
        newTask.setId(task.getId());
        switch (currentStatus) {

            case NEW -> newTask.setStatus(Status.IN_PROGRESS);

            case IN_PROGRESS, DONE -> newTask.setStatus(Status.DONE);

        }
        taskList.put(newTask.getId(), newTask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Status currentStatus = subtask.getStatus();
        Subtask newSubtask = createSubtask(subtask.getTitle(), subtask.getDescription(),
                subtask.getEpicId());
        idCount--;
        newSubtask.setId(subtask.getId());
        switch (currentStatus) {

            case NEW -> newSubtask.setSubtaskStatus(Status.IN_PROGRESS);

            case IN_PROGRESS, DONE -> newSubtask.setSubtaskStatus(Status.DONE);
        }
        subtaskList.put(newSubtask.getId(), newSubtask);
        updateEpic(epicList.get(newSubtask.getEpicId()));
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic newEpic = createEpic(epic.getTitle(), epic.getDescription());
        idCount--;
        newEpic.setId(epic.getId());
        updateEpicStatus(newEpic);
    }

    public void updateEpicStatus(Epic epic){
        int subtasksInDone = 0;
        int subtasksInProgress = 0;
        for (Subtask subtask : getEpicSubtasks(epic.getId())) {
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

        if (subtasksInDone == getEpicSubtasks(epic.getId()).size()) {
            epic.setStatus(Status.DONE);
            epicList.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteEpicSubtasks(int epicId) {
        subtaskList.values().removeAll(getEpicSubtasks(epicId));
        epicList.get(epicId).setStatus(Status.DONE);
        updateEpic(epicList.get(epicId));
    }

    @Override
    public <T extends Task> List<T> getHistory(){
        return (List<T>) inMemoryHistoryManager.getViewHistory();
    }
}



