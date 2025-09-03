import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.time.LocalDateTime;

public class SubtaskTest {
    public InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    public void cleanAll() {
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.epicList.clear();
        inMemoryTaskManager.idCount = 0;
    }

    @Test
//экземпляры Subtask равны друг другу, если равны их id
    public void subtaskEqualsSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        Subtask secondSubtask = firstSubtask;

        Assertions.assertEquals(firstSubtask.getId(), secondSubtask.getId());
        Assertions.assertEquals(firstSubtask, secondSubtask);
    }

    @Test
    public void subtaskDurationAndStartTimeNotChangedAfterAddingInTaskList() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        firstSubtask.setDuration(15);
        firstSubtask.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(firstSubtask, inMemoryTaskManager.subtaskList);


        Assertions.assertEquals("PT15M", firstSubtask.getDuration().toString());
        Assertions.assertEquals(LocalDateTime.of(2025, 1, 1, 1, 1), firstSubtask.getStartTime());
    }

    @Test
    public void subtasksHasEpicIdToExistingEpic() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        Assertions.assertTrue(inMemoryTaskManager.getPrioritizedTasks().contains(inMemoryTaskManager.findTask(subtask2.getEpicId())));
        Assertions.assertTrue(inMemoryTaskManager.getPrioritizedTasks().contains(inMemoryTaskManager.findTask(subtask3.getEpicId())));
    }
}