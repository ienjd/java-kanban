package Tasks;

import Enums.Statuses;
import Main.Main;
import Main.Main.TaskMaster;

public class Subtask extends Task{
    private int epicId;

    public int getEpicId(){
        return epicId;
    }

    public void setEpicId(int epicId){
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, Statuses status) {
        super(title, description, id, status);
    }

    public static Subtask createTask(String title, String description) {
        Subtask newTask = new Subtask(title, description, TaskMaster.getIdCount() + 1, Statuses.NEW);
        TaskMaster.setIdCount();
        return newTask;
    }

    public static void addSubtaskToList(Subtask subtask) {
        TaskMaster.SUBTASK_LIST.put(subtask.getId(), subtask);
    }

    public static Subtask updateSubtask(int id){

        Subtask subtask = (Subtask) TaskMaster.findTask(id);
        Statuses status = ((Subtask) TaskMaster.findTask(id)).status == Statuses.NEW ? subtask.setStatus(Statuses.IN_PROGRESS)
                : subtask.setStatus(Statuses.DONE);
        subtask.setStatus(status);
        addSubtaskToList(subtask);
        Main.TaskMaster.getSubtaskList().put(subtask.id, subtask);
        Epic.changeEpicStatus(subtask.checkSubtasksThisEpic());
        return subtask;
    }

    public int checkSubtasksThisEpic(){
        int idEpicInProgress = 0;
        for (Subtask subtask : Epic.subtasksThisEpic(getEpicId())) {
            if(subtask.status.equals(Statuses.IN_PROGRESS) || (subtask.status.equals(Statuses.DONE))){
                idEpicInProgress = subtask.getEpicId();
            }
        }
        return idEpicInProgress;
    }

    public String toString() {
        return "Task{ " +
                "title= " + title + '\'' +
                ", description= " + description + '\'' +
                ", id= " + id +
                ", status= " + status + ", class= " +
                getClass() + " " + ", epicId= " + epicId + " " +
                " }";
    }
}
