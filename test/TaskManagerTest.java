import exceptions.ManagerSaveException;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;

import static tasks.Status.*;
import static tasks.Status.DONE;

public abstract class TaskManagerTest<T extends TaskManager> {

    public InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    public void cleanAll() {
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.epicList.clear();
        inMemoryTaskManager.idCount = 0;
    }

    @Test
    public void createTask() {
        Task firstTask = inMemoryTaskManager.createTask("Первая задача", "Описание первой задачи");

        Assertions.assertNotEquals(null, firstTask);
    }

    @Test
    public void createEpic() throws ManagerSaveException {
        Epic epic = inMemoryTaskManager.createEpic("ER", "ER");

        Assertions.assertNotEquals(null, epic);
    }

    @Test
    public void creatingSubtask() {
        Subtask subtask = inMemoryTaskManager.createSubtask("ER", "ER", 1);

        Assertions.assertNotEquals(null, subtask);
    }

    @Test
    public void addTaskToList() throws ManagerSaveException {
        Task task = new Task("Первая задача", "Описание первой задачи", 1, NEW);
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Assertions.assertEquals(task, inMemoryTaskManager.findTask(1));
    }

    @Test
    public void findTask() throws ManagerSaveException {
        Task task = new Task("Первая задача", "Описание первой задачи", 1, NEW);
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Assertions.assertEquals(task, inMemoryTaskManager.findTask(1));
    }

    @Test
    public void deleteTaskFromList() throws IOException {
        Task task = new Task("Первая задача", "Описание первой задачи", 1, NEW);
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Assertions.assertEquals(task, inMemoryTaskManager.findTask(1));

        inMemoryTaskManager.deleteTaskFromList(1);

        Assertions.assertFalse(inMemoryTaskManager.taskList.containsKey(1));
    }

    @Test
    public void getEpicSubtasks() throws ManagerSaveException {
        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        Assertions.assertTrue(inMemoryTaskManager.getEpicSubtasks(1).contains(subtask1));
        Assertions.assertTrue(inMemoryTaskManager.getEpicSubtasks(1).contains(subtask2));
        Assertions.assertTrue(inMemoryTaskManager.getEpicSubtasks(1).contains(subtask3));
    }

    @Test
    public void updateTask() throws ManagerSaveException {

        Task task1 = inMemoryTaskManager.createTask("task1", "task1");
        task1.setDuration(15);
        task1.setStartTime(LocalDateTime.now());
        inMemoryTaskManager.addTaskToList(task1, inMemoryTaskManager.taskList);
        Assertions.assertEquals(NEW, task1.getStatus());

        inMemoryTaskManager.updateTask(task1);
        Assertions.assertEquals(IN_PROGRESS, inMemoryTaskManager.findTask(1).getStatus());
    }

    @Test
    public void updateSubtask() throws ManagerSaveException {
        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        inMemoryTaskManager.updateSubtask(subtask3);

        Assertions.assertEquals(IN_PROGRESS, inMemoryTaskManager.findTask(2).getStatus());
        Assertions.assertEquals(IN_PROGRESS, inMemoryTaskManager.findTask(1).getStatus());
    }

    @Test
    public void updateEpic() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);
        Assertions.assertEquals(NEW, epic1.getStatus());

        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        subtask3.setStatus(DONE);
        inMemoryTaskManager.updateEpicStatus(epic1);
        Assertions.assertEquals(DONE, epic1.getStatus());
    }

    @Test
    public void deleteEpicSubtasks() throws ManagerSaveException {
        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);

        Assertions.assertFalse(inMemoryTaskManager.subtaskList.isEmpty());

        inMemoryTaskManager.deleteEpicSubtasks(1);
        Assertions.assertTrue(inMemoryTaskManager.subtaskList.isEmpty());
    }

    @Test
    public void getHistory() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        inMemoryTaskManager.findTask(1);
        inMemoryTaskManager.findTask(2);

        Assertions.assertFalse(inMemoryTaskManager.getHistory().isEmpty());
        Assertions.assertTrue(inMemoryTaskManager.getHistory().contains(subtask3));
        Assertions.assertTrue(inMemoryTaskManager.getHistory().contains(epic1));
    }
}