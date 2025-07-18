
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

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
}