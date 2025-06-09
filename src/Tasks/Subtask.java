package Tasks;

import Enums.Statuses;
import Main.Main;

public class Subtask extends Task{
    private int epicId;

    public Subtask(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }

    public static boolean taskIsInList(int taskId) {
        boolean isInTaskList = Main.TaskMaster.SUBTASK_LIST.containsKey(taskId) ? true : false;
        return isInTaskList;
    }
}
