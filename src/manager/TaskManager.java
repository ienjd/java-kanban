package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void addTaskToList(Task task, HashMap hashmap) throws ManagerSaveException;

    Task createTask(String title, String description);

    Epic createEpic(String title, String description);

    Subtask createSubtask(String title, String description, int epicId);

    <T extends Task> T findTask(int id);

    void deleteTaskFromList(int id) throws IOException;

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    void updateTask(Task task) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    void deleteEpicSubtasks(int epicId) throws ManagerSaveException;

    List getHistory();
}
