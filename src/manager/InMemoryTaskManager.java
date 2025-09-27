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
    private int idCount = 0;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private TreeSet<Task> sortedTasks = new TreeSet<>();

    private int getIdCount() {
        return idCount;
    }

    private void addIdCount() {
        idCount++;
    }

    public void clearTaskList() {
        taskList.clear();
    }

    public void clearEpicList() {
        epicList.clear();
    }

    public void clearSubtaskList() {
        subtaskList.clear();
    }

    public void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    public <T extends Task> ArrayList <T> getAllTasks() {
        List<T> allTasks = new ArrayList<>();
        allTasks.addAll((Collection<T>) getTaskList().values());
        allTasks.addAll((Collection<T>) getSubtaskList().values());
        allTasks.addAll((Collection<T>) getEpicList().values());
        return (ArrayList<T>) allTasks;
    }

    public void deleteAllTasks() {
        clearTaskList();
        clearEpicList();
        clearSubtaskList();
    }

    @Override
    public void addTaskToList(Task task, HashMap hashMap) throws ManagerSaveException {
        if (!isOverLapping(task)) {
            hashMap.put(task.getId(), task);
        } else {
            throw new ManagerSaveException("Добавляемая задача пересекается по времени выполнения с уже существующими");
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
        Epic epic = epicList.get(epicId);
        if (epic == null) {
            throw new IllegalStateException("Epic " + epicId + " not found for subtask " + epicId);
        }
        fillSubtasks(epic, subtask);
        setEpicStartTime(epicId);
        setEpicDuration(epicId);
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
    public void updateTask(Task task, Status status) throws ManagerSaveException {
        task.setStatus(status);
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, Status status) throws ManagerSaveException {
        subtask.setStatus(status);
        subtaskList.put(subtask.getId(), subtask);
        fillSubtasks(epicList.get(subtask.getEpicId()), subtask);
        setEpicDuration(subtask.getEpicId());

        setEpicStartTime(subtask.getEpicId());
        updateEpicStatus(epicList.get(subtask.getEpicId()));
    }

    public void fillSubtasks(Epic epic, Subtask subtask) {
        Objects.requireNonNull(epic, "epic is null");
        Objects.requireNonNull(subtask, "subtask is null");
        epic.setSubtasks(subtask);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        updateEpicStatus(epic);
        epicList.put(epic.getId(), epic);
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

    private <T extends Task> boolean isOverLapping(T nonSortedTask) throws ManagerSaveException {
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



