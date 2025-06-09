package Tasks;

import Enums.Statuses;
import Main.Main;
import UserInputs.UserInputs;

import java.util.ArrayList;

public class Epic extends Task {

    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }

    public static Epic changeEpicStatus(int id) {
        Statuses status;
        int subtasksInDone = 0;
        Epic epic = (Epic) Main.TaskMaster.findTask(id);
        for (Subtask subtask : subtasksThisEpic(id)) {
            if (subtask.status.equals(Statuses.DONE)) {
                subtasksInDone++;
            }
        }
        status = subtasksInDone == subtasksThisEpic(id).size() ? Statuses.DONE : Statuses.IN_PROGRESS;
        epic.setStatus(status);
        addTaskToList(epic);
        Main.TaskMaster.EPIC_LIST.put(epic.id, epic);
        return epic;
    }

    public static Epic createTask(String title, String description) {
        Epic newTask = new Epic(title, description, Main.TaskMaster.getIdCount() + 1, Statuses.NEW);
        Main.TaskMaster.setIdCount();
        UserInputs userInputs = new UserInputs();
        System.out.println("Введите количество подзадач в данном эпике: ");
        int totalSubtasks = Integer.parseInt(userInputs.getUserInput());
        for (int i = 0; i < totalSubtasks; i++) {
            Subtask subtask = Subtask.createTask(userInputs.getTitle(), userInputs.getDescription());
            subtask.setEpicId(newTask.getId());
            Subtask.addSubtaskToList(subtask);
        }
        return newTask;
    }

    public static void addTaskToList(Epic epic) {
        Main.TaskMaster.EPIC_LIST.put(epic.getId(), epic);
    }

    public static ArrayList<Subtask> subtasksThisEpic(int epicId) {
        ArrayList<Subtask> subtasksThisEpic = new ArrayList<>();
        for (Subtask subtask : Main.TaskMaster.getSubtaskList().values()) {
            if (subtask.getEpicId() == epicId) {
                subtasksThisEpic.add(subtask);
            }
        }
        return subtasksThisEpic;
    }
}
