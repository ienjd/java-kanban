package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

public class InMemoryHistoryManagerTest extends TaskManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    public void clearHistory(){
        for (int i = 0; i < inMemoryHistoryManager.getHistory().size(); i++) {
            inMemoryHistoryManager.remove(i);
        }
        inMemoryTaskManager.idCount = 0;
    }

    @Test
    public void historyIsEmpty(){
        Assertions.assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }


    @Test
// Данный метод проверяет что в список просмотренных задач попадает только одна задача в актуальном состоянии на момент добавления
    public void onlyActualVersionOfTaskAddedToHistoryView() {
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

    @Test
    public void deletingFromHeadOfList(){
        Task task = inMemoryTaskManager.createTask("Первая таска", "Описание первой таски");
        inMemoryHistoryManager.add(task);
        Task taskTwo = inMemoryTaskManager.createTask("Вторая таска", "Описание второй таски");
        inMemoryHistoryManager.add(taskTwo);
        inMemoryHistoryManager.remove(1);

        Assertions.assertEquals(inMemoryHistoryManager.getHistory().size(), 1);
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getFirst(), taskTwo);
    }

    @Test
    public void deletingFromEndOfList(){
        Task task = inMemoryTaskManager.createTask("Первая таска", "Описание первой таски");
        inMemoryHistoryManager.add(task);
        Task taskTwo = inMemoryTaskManager.createTask("Вторая таска", "Описание второй таски");
        inMemoryHistoryManager.add(taskTwo);
        inMemoryHistoryManager.remove(2);

        Assertions.assertEquals(inMemoryHistoryManager.getHistory().size(), 1);
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getLast(), task);
    }

    @Test
    public void deletingFromMiddleOfList(){
        Task task = inMemoryTaskManager.createTask("Первая таска", "Описание первой таски");
        inMemoryHistoryManager.add(task);
        Task taskTwo = inMemoryTaskManager.createTask("Вторая таска", "Описание второй таски");
        inMemoryHistoryManager.add(taskTwo);
        Task taskThree = inMemoryTaskManager.createTask("Третья таска", "Описание третьей таски");
        inMemoryHistoryManager.add(taskThree);
        inMemoryHistoryManager.remove(2);

        Assertions.assertEquals(inMemoryHistoryManager.getHistory().size(), 2);
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getLast(), taskThree);
        Assertions.assertEquals(inMemoryHistoryManager.getHistory().getFirst(), task);
    }
}