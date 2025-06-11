package TaskMaster;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

    public class TaskMaster {
        public static int idCount = 0;
        public final HashMap<Integer, Task> taskList = new HashMap<>();
        public final HashMap<Integer, Epic> epicList = new HashMap<>();
        public final HashMap<Integer, Subtask> subtaskList = new HashMap<>();


        public static int getIdCount() {
            return idCount;
        }

        public static void setIdCount() {
            idCount++;
        }

        public void addTaskToList(Task task, HashMap hashMap) {
            hashMap.put(task.getId(), task);
        }

        public Task createTask(String title, String description) {
            Task task = new Task(title, description, idCount + 1, Status.NEW);
            setIdCount();
            return task;
        }

        public Epic createEpic(String title, String description) {
            Epic epic = new Epic(title, description, idCount + 1, Status.NEW);
            setIdCount();
            return epic;
        }

        public Subtask createSubtask(String title, String description, int epicId) {
            Subtask subtask = new Subtask(title, description, TaskMaster.getIdCount() + 1, Status.NEW, epicId);
            setIdCount();
            return subtask;
        }

        public Object findTask(int findId) {

            Object object = null;
            if (taskList.containsKey(findId)) {
                object = taskList.get(findId);
            } else if (epicList.containsKey(findId)) {
                object = epicList.get(findId);
            } else if (subtaskList.containsKey(findId)) {
                object = subtaskList.get(findId);
            }
            return object;
        }

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

        public ArrayList<Subtask> subtasksThisEpic(int epicId) {
            ArrayList<Subtask> subtasksThisEpic = new ArrayList<>();
            for (Subtask subtask : subtaskList.values()) {
                if (subtask.getEpicId() == epicId) {
                    subtasksThisEpic.add(subtask);
                }
            }
            return subtasksThisEpic;
        }

        public void updateTask(int id) {
            Task task = taskList.get(id);
            Status status = task.getStatus();
            switch (status) {

                case Status.NEW -> task.setStatus(Status.IN_PROGRESS);

                case Status.IN_PROGRESS -> task.setStatus(Status.DONE);
            }
            taskList.put(task.getId(), task);
        }

        public void updateSubtask(int id) {
            if (subtaskList.get(id).getStatus().equals(Status.NEW)) {
                subtaskList.get(id).setSubtaskStatus(Status.IN_PROGRESS);
            } else if (subtaskList.get(id).getStatus().equals(Status.IN_PROGRESS)) {
                subtaskList.get(id).setSubtaskStatus(Status.DONE);
            }
            subtaskList.put(subtaskList.get(id).getId(), subtaskList.get(id));
            updateEpic(subtaskList.get(id).getEpicId());
        }

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
    }


