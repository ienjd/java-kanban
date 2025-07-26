package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager{
    String path;

    public FileBackedTaskManager(String path){
        this.path = path;
    }

    private void save(){

    }

    @Override
    public void addTaskToList(Task task, HashMap hashMap) {
        super.addTaskToList(task, hashMap);
        save();
    }

    @Override
    public void deleteTaskFromList(int id) {
        super.deleteTaskFromList(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}
