package InMemoryTaskManager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    void addTaskToList(Task task, HashMap hashMap);

    Task createTask(String title, String description);

    Epic createEpic(String title, String description);

    Subtask createSubtask(String title, String description, int epicId);

    Object findTask(int findId) throws CloneNotSupportedException;

    void deleteTaskFromList(int id);

    ArrayList<Subtask> subtasksThisEpic(int epicId);

    void updateTask(int id);

    void updateSubtask(int id);

    void updateEpic(int id);

    void deleteSubtasksThisEpic(int epicId);

}
