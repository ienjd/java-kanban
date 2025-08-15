
import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

class TaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
//экземпляры Task равны друг другу, если равны их id
    void taskEqualsTask() {
        Task firstTask = inMemoryTaskManager.createTask("Первая задача", "Описание первой задачи");
        Task secondTask = firstTask;

        Assertions.assertEquals(firstTask.getId(), secondTask.getId());
        Assertions.assertEquals(firstTask, secondTask);
    }

    @Test
    void taskDurationAndStartTimeNotChangedAfterAddingInTaskList() throws ManagerSaveException {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task firstTask = inMemoryTaskManager.createTask("Первая подзадача", "Описание первой подзадачи");
        firstTask.setDuration(15);
        firstTask.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(firstTask, inMemoryTaskManager.taskList);


        Assertions.assertEquals("PT15M", firstTask.getDuration().toString());
        Assertions.assertEquals(LocalDateTime.of(2025, 1, 1, 1, 1), firstTask.getStartTime());
    }

}