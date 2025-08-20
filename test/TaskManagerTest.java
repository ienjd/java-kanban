import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import java.io.IOException;

abstract class TaskManagerTest<T extends TaskManager> {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    public void createTask() {
    }

    @Test
    public void createEpic() throws ManagerSaveException {
    }

    @Test
    public void createSubtask() {
    }

    @Test
    public void addTaskToList() throws ManagerSaveException {
    }

    @Test
    public void findTask() throws ManagerSaveException {
    }

    @Test
    public void deleteTaskFromList() throws IOException {
    }

    @Test
    public void getEpicSubtasks() throws ManagerSaveException {
    }

    @Test
    public void updateTask() throws ManagerSaveException {
    }

    @Test
    public void updateSubtask() throws ManagerSaveException {
    }

    @Test
    public void updateEpic() throws ManagerSaveException {
    }

    @Test
    public void deleteEpicSubtasks() throws ManagerSaveException {
    }

    @Test
    public void getHistory() throws ManagerSaveException {
    }
}