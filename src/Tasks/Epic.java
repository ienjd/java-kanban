package Tasks;

import Enums.Statuses;
import Main.Main;
import UserInputs.UserInputs;

import java.util.ArrayList;

import static Main.Main.TaskMaster.getSubtaskList;

public class Epic extends Task {

    public Epic(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }


//вот здесь нужно прописать ветвление проверки всех статусов подзадач
    public static Epic changeEpicStatus(int id){
        Epic epic = (Epic) Main.TaskMaster.findTask(id);
        Statuses status = ((Task) Main.TaskMaster.findTask(id)).status == Statuses.NEW ? epic.setStatus(Statuses.IN_PROGRESS)
                : epic.setStatus(Statuses.DONE);
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
            Subtask subtask = Subtask.createTask(userInputs.getUserInput(), userInputs.getUserInput());
            subtask.setEpicId(newTask.getId());
            Subtask.addSubtaskToList(subtask);
        }
        return newTask;
    }

    public static void addTaskToList(Epic epic) {
        Main.TaskMaster.EPIC_LIST.put(epic.getId(), epic);
    }
    public static ArrayList<Subtask> subtasksThisEpic(int epicId){
        ArrayList<Subtask> subtasksThisEpic = new ArrayList<>();
        for (Subtask subtask : Main.TaskMaster.getSubtaskList().values()) {
            if(subtask.getEpicId()==epicId){
                subtasksThisEpic.add(subtask);
            }
        }
        return subtasksThisEpic;
    }
}
