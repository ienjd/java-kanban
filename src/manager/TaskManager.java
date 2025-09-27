package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    public void addTaskToList(Task task, HashMap hashMap) throws ManagerSaveException;

    public Task createTask(String title, String description);

    public Epic createEpic(String title, String description) throws ManagerSaveException;

    public Subtask createSubtask(String title, String description, int epicId);

    public <T extends Task> T findTask(int id);

    public void deleteTaskFromList(int id) throws IOException;

    public ArrayList<Subtask> getEpicSubtasks(int epicId);

    public void updateTask(Task task, Status status) throws ManagerSaveException;

    public void updateSubtask(Subtask subtask, Status status) throws ManagerSaveException;

    public void updateEpic(Epic epic) throws ManagerSaveException;

    public void deleteEpicSubtasks(int epicId) throws ManagerSaveException;

    public List getHistory();
}
