package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager<T> implements TaskManager {
    public static int idCount = 0;
    public final HashMap<Integer, Task> taskList = new HashMap<>();
    public final HashMap<Integer, Epic> epicList = new HashMap<>();
    public final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    public TreeSet<Task> sortedTasks = new TreeSet<>();

    public static int getIdCount() {
        return idCount;
    }

    public static void addIdCount() {
        idCount++;
    }

    @Override
    public void addTaskToList(Task task, HashMap hashMap) throws ManagerSaveException {
        sortingTasks();
        if(sortedTasks.isEmpty()){
            hashMap.put(task.getId(), task);
        }

        if (!isTimeLapsesIntersects(task)) {
            hashMap.put(task.getId(), task);
        }

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
        Optional<LocalDateTime> minStart = getEpicSubtasks(epic.getId()).stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        epic.setStartTime(minStart.orElse(null));
        long totalMinutes = getEpicSubtasks(epic.getId()).stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .mapToLong(Duration::toMinutes)
                .sum();
        epic.setDuration((int) totalMinutes);
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
            epicList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            int epicIdRemovedSubtask = subtaskList.get(id).getEpicId();
            forgetTask(id);
            subtaskList.remove(id);
            updateEpic(epicList.get(epicIdRemovedSubtask));
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
        updateEpic(epicList.get(newSubtask.getEpicId()));
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        Epic newEpic = createEpic(epic.getTitle(), epic.getDescription());
        idCount--;
        newEpic.setId(epic.getId());
        updateEpicStatus(newEpic);
    }

    public void updateEpicStatus(Epic epic) {
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
    public void deleteEpicSubtasks(int epicId) throws ManagerSaveException {
        getEpicSubtasks(epicId).stream()
                .map(Subtask::getId)
                .forEach(id -> forgetTask(id));
        subtaskList.values().removeAll(getEpicSubtasks(epicId));
        epicList.get(epicId).setStatus(Status.DONE);
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
        long totalMinutes = getEpicSubtasks(epicId).stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .mapToLong(Duration::toMinutes)
                .sum();
        findTask(epicId).setDuration((int) totalMinutes);
    }

    public void setEpicStartTime(int epicId) {
        Optional<LocalDateTime> minStart = getEpicSubtasks(epicId).stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
                epicList.get(epicId).setStartTime(minStart.orElse(null));
    }

    public void sortingTasks() throws ManagerSaveException {
        epicList.values().stream().map( epic -> {
                    List<Subtask> subs = getEpicSubtasks(epic.getId());
                    if (subs.isEmpty()) {
                        epic.setDuration(0);
                        epic.setStartTime(null);
                    } else {
                        setEpicDuration(epic.getId());
                        setEpicStartTime(epic.getId());
                    }
                    return null;
                }
        );

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(taskList.values());
        allTasks.addAll(epicList.values());
        allTasks.addAll(subtaskList.values());

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

    public <T extends Task> boolean isTimeLapsesIntersects(T nonSortedTask) {
        boolean isIntersect = sortedTasks.stream()
                    .filter(sortedTask -> (sortedTask.getStartTime().isAfter(nonSortedTask.getStartTime()) &&
                            sortedTask.getStartTime().isBefore(nonSortedTask.getEndTime())) ||
                            (sortedTask.getEndTime().isAfter(nonSortedTask.getStartTime()) &&
                                    (sortedTask.getEndTime().isBefore(nonSortedTask.getEndTime()))))
                    .findFirst()
                    .isPresent();
        return isIntersect;
    }
}



