
import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
//экземпляры Subtask равны друг другу, если равны их id
    void subtaskEqualsSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        Subtask secondSubtask = firstSubtask;

        Assertions.assertEquals(firstSubtask.getId(), secondSubtask.getId());
        Assertions.assertEquals(firstSubtask, secondSubtask);
    }
    @Test
    void subtaskDurationAndStartTimeNotChangedAfterAddingInTaskList() throws ManagerSaveException {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        firstSubtask.setDuration(15);
        firstSubtask.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(firstSubtask, inMemoryTaskManager.subtaskList);


        Assertions.assertEquals("PT15M", firstSubtask.getDuration().toString());
        Assertions.assertEquals(LocalDateTime.of(2025, 1, 1, 1, 1), firstSubtask.getStartTime());
    }
}