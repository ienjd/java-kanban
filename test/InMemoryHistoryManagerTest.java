
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
// Данный метод проверяет что в список просмотренных задач попадает только одна задача в актуальном состоянии на момент добавления
    void onlyActualVersionOfTaskAddedToHistoryView() {
        Task task = inMemoryTaskManager.createTask("Первоначальный заголовок", "Первоначальное описание");
        Task taskTwo = task;
        Task taskThree = inMemoryTaskManager.createTask("Третья таска", "Описание третьей таски");
        inMemoryHistoryManager.add(task);
        taskTwo.setDescription("Новое описание");
        inMemoryHistoryManager.add(taskTwo);
        inMemoryHistoryManager.add(taskThree);
        Task taskFour = (Task) inMemoryHistoryManager.getHistory().getFirst();

        Assertions.assertEquals(2, inMemoryHistoryManager.getHistory().size());
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getFirst(), taskTwo);
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getLast(), taskThree);
        Assertions.assertEquals("Новое описание", taskFour.getDescription());
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().size(), inMemoryHistoryManager.getHistory().size());
    }
}