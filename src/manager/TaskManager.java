package manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
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

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void deleteEpicSubtasks(int epicId);

    <T extends Task> List<T> getHistory();
}
