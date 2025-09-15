package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    public static int idCount = 0;
    public final HashMap<Integer, Task> taskList = new HashMap<>();
    public final HashMap<Integer, Epic> epicList = new HashMap<>();
    public final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    public TreeSet<Task> sortedTasks = new TreeSet<>();

    public static int getIdCount() {
        return idCount;
    }

    public static void addIdCount() {
        idCount++;
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    @Override
    public void addTaskToList(Task task, HashMap hashMap) throws ManagerSaveException {

        if (task instanceof Subtask) {
            Subtask st = (Subtask) task;
            Epic epic = epicList.get(st.getEpicId());
            if (epic == null) {
                throw new IllegalStateException("Epic " + st.getEpicId() + " not found for subtask " + st.getId());
            }
            fillSubtasks(epic, st);
            setEpicStartTime(st.getEpicId());
            setEpicDuration(st.getEpicId());
        }

        if (!isOverLapping(task)) {
            hashMap.put(task.getId(), task);
        }
        sortingTasks();
    }

    @Override
    public Task createTask(String title, String description) {
        addIdCount();
        Task task = new Task(title, description, getIdCount(), Status.NEW);
        return task;
    }

    @Override
    public Epic createEpic(String title, String description) throws ManagerSaveException {
        addIdCount();
        Epic epic = new Epic(title, description, getIdCount(), Status.NEW);
        addTaskToList(epic, epicList);
        setEpicStartTime(epic.getId());
        setEpicDuration(epic.getId());
        return epic;
    }

    @Override
    public Subtask createSubtask(String title, String description, int epicId) {
        addIdCount();
        int newId = getIdCount();
        if (!epicList.containsKey(epicId)) {
            throw new IllegalArgumentException("Epic " + epicId + " not found");
        }
        Subtask subtask = new Subtask(title, description, newId, Status.NEW, epicId);
        return subtask;
    }

    @Override
    public <T extends Task> T findTask(int findId) {
        T object = null;
        if (taskList.containsKey(findId)) {
            object = (T) taskList.get(findId);
        } else if (epicList.containsKey(findId)) {
            object = (T) epicList.get(findId);
        } else if (subtaskList.containsKey(findId)) {
            object = (T) subtaskList.get(findId);
        }
        inMemoryHistoryManager.add(object);
        return object;
    }

    @Override
    public void deleteTaskFromList(int id) throws IOException {
        if (taskList.containsKey(id)) {
            forgetTask(id);
            taskList.remove(id);
        } else if (epicList.containsKey(id)) {
            forgetTask(id);
            deleteEpicSubtasks(id);
            epicList.get(id).clearSubtasksList();
            epicList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            int epicIdRemovedSubtask = subtaskList.get(id).getEpicId();
            epicList.get(epicIdRemovedSubtask).deleteSubtasks(id);
            forgetTask(id);
            subtaskList.remove(id);
            updateEpicStatus(epicList.get(epicIdRemovedSubtask));
        } else {
            System.out.println("Данного элемента уже нет в списке");
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtasksThisEpic = (ArrayList<Subtask>) subtaskList.values().stream()
                .filter(subtask -> (subtask.getEpicId() == epicId))
                .collect(Collectors.toList());
        return subtasksThisEpic;
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        Status currentStatus = task.getStatus();
        Task newTask = createTask(task.getTitle(), task.getDescription());
        newTask.duration = task.duration;
        newTask.setStartTime(task.getStartTime());
        idCount--;
        newTask.setId(task.getId());
        switch (currentStatus) {

            case NEW -> newTask.setStatus(Status.IN_PROGRESS);

            case IN_PROGRESS, DONE -> newTask.setStatus(Status.DONE);

        }
        taskList.put(newTask.getId(), newTask);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        Status currentStatus = subtask.getStatus();
        Subtask newSubtask = createSubtask(subtask.getTitle(), subtask.getDescription(), subtask.getEpicId());
        idCount--;
        newSubtask.setId(subtask.getId());
        newSubtask.setStartTime(subtask.getStartTime());
        newSubtask.duration = subtask.getDuration();
        switch (currentStatus) {

            case NEW -> newSubtask.setSubtaskStatus(Status.IN_PROGRESS);

            case IN_PROGRESS, DONE -> newSubtask.setSubtaskStatus(Status.DONE);
        }
        subtaskList.put(newSubtask.getId(), newSubtask);
        fillSubtasks(epicList.get(newSubtask.getEpicId()), newSubtask);
        setEpicDuration(newSubtask.getEpicId());

        setEpicStartTime(newSubtask.getEpicId());
        updateEpicStatus(epicList.get(newSubtask.getEpicId()));
    }

    public void fillSubtasks(Epic epic, Subtask subtask) {
        Objects.requireNonNull(epic, "epic is null");
        Objects.requireNonNull(subtask, "subtask is null");
        epic.setSubtasks(subtask);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        Epic newEpic = createEpic(epic.getTitle(), epic.getDescription());
        idCount--;
        newEpic.setId(epic.getId());
        updateEpicStatus(newEpic);
    }

    public void updateEpicStatus(Epic epic) {
        int subtasksInNew = 0;
        int subtasksInDone = 0;
        int subtasksInProgress = 0;
        for (Subtask subtask : getEpicSubtasks(epic.getId())) {
            if (subtask.getStatus().equals(Status.NEW)) {
                subtasksInNew++;
            } else if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                subtasksInProgress++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                subtasksInDone++;
            }
        }
        if (subtasksInNew == getEpicSubtasks(epic.getId()).size()) {
            epic.setStatus(Status.NEW);
            epicList.put(epic.getId(), epic);
        } else if (subtasksInDone == getEpicSubtasks(epic.getId()).size()) {
            epic.setStatus(Status.DONE);
            epicList.put(epic.getId(), epic);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
            epicList.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteEpicSubtasks(int epicId) throws ManagerSaveException {
        getEpicSubtasks(epicId).stream()
                .map(Subtask::getId)
                .forEach(id -> forgetTask(id));
        subtaskList.values().removeAll(getEpicSubtasks(epicId));
        epicList.get(epicId).setStatus(Status.DONE);
        epicList.get(epicId).clearSubtasksList();
        updateEpic(epicList.get(epicId));
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public void forgetTask(int id) {
        inMemoryHistoryManager.remove(id);
    }

    public void setEpicDuration(int epicId) {
        if (!getEpicSubtasks(epicId).isEmpty()) {
            long duration = getEpicSubtasks(epicId).stream()
                    .map(Subtask::getDuration)
                    .filter(Objects::nonNull)
                    .mapToLong(Duration::toMinutes)
                    .sum();
            epicList.get(epicId).setDuration((int) duration);
        } else {
            epicList.get(epicId).setDuration(0);
        }
    }

    public void setEpicStartTime(int epicId) {
        Optional<LocalDateTime> minStart = getEpicSubtasks(epicId).stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        epicList.get(epicId).setStartTime(minStart.orElse(LocalDateTime.now()));
    }

    public void sortingTasks() throws ManagerSaveException {
        if (!epicList.isEmpty()) {
            epicList.values().stream().map(epic -> {
                        List<Subtask> subs = getEpicSubtasks(epic.getId());
                        if (subs.isEmpty()) {
                            epic.setDuration(0);
                            epic.setStartTime(LocalDateTime.of(0, 0, 0, 0, 0));
                        } else {
                            setEpicDuration(epic.getId());
                            setEpicStartTime(epic.getId());
                        }
                        return epic;
                    }
            );
        }

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(taskList.values());
        allTasks.addAll(epicList.values());

        Comparator<Task> comparator = (a, b) -> {
            LocalDateTime timeA = a.getStartTime();
            LocalDateTime timeB = b.getStartTime();
            if (timeA == null && timeB == null) {
                return Integer.compare(a.getId(), b.getId());
            } else if (timeA == null) {
                return 1;
            } else if (timeB == null) {
                return -1;
            } else {
                int timeCompare = timeA.compareTo(timeB);
                if (timeCompare != 0) {
                    return timeCompare;
                } else {
                    return Integer.compare(a.getId(), b.getId());
                }
            }
        };

        sortedTasks = new TreeSet<>(comparator);
        sortedTasks.addAll(allTasks);
    }

    public TreeSet getPrioritizedTasks() {
        return sortedTasks;
    }

    public <T extends Task> boolean isOverLapping(T nonSortedTask) throws ManagerSaveException {
        sortingTasks();
        return sortedTasks.stream()
                .filter(sortedTask -> (sortedTask.getStartTime().isEqual(nonSortedTask.getStartTime()) &&
                        sortedTask.getEndTime().isAfter(nonSortedTask.getStartTime()) ||
                        (nonSortedTask.getStartTime().isEqual(sortedTask.getStartTime()) ||
                                (nonSortedTask.getStartTime().isBefore(sortedTask.getStartTime()))) &&
                                (nonSortedTask.getEndTime().isAfter(sortedTask.getStartTime()))))
                .findFirst()
                .isPresent();

    }
}



