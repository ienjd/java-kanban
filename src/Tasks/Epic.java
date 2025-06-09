package Tasks;

import Enums.Statuses;
import Main.Main;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> idSubtasksThisEpic = new ArrayList<>();


    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }


    public static Epic createTask(String title, String description) {
        Epic newTask = new Epic(title, description, Main.TaskMaster.getIdCount() + 1, Statuses.NEW);
        Main.TaskMaster.setIdCount();
        return newTask;
    }

    public boolean findTask(int taskId) {
        boolean isInEpicList = false;
        if (Main.TaskMaster.EPIC_LIST.containsKey(taskId)) {
            isInEpicList = true;
        }
        return isInEpicList;
    }

    public static boolean taskIsInList(int taskId) {
        boolean isInTaskList = Main.TaskMaster.EPIC_LIST.containsKey(taskId) ? true : false;
        return isInTaskList;
    }


    public static void addTaskToList(Epic epic) {
        Main.TaskMaster.EPIC_LIST.put(epic.getId(), epic);
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
