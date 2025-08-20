package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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