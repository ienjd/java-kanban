package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test// Данный метод проверяет что в список просмотренных задач попадает только одна задача в актуальном состоянии на момент добавления
    void onlyActualVersionOfTaskAddedToHistoryView() {
        Task task = inMemoryTaskManager.createTask("Первоначальный заголовок", "Первоначальное описание");
        Task taskTwo = task;
        Task taskThree = inMemoryTaskManager.createTask("Третья таска", "Описание третьей таски");
        inMemoryHistoryManager.add(task);
        taskTwo.setDescription("Новое описание");
        inMemoryHistoryManager.add(taskTwo);
        inMemoryHistoryManager.add(taskThree);
        Task taskFour = (Task)inMemoryHistoryManager.getTasks().getFirst();

        Assertions.assertTrue(inMemoryHistoryManager.getTasks().size() == 2);
        Assertions.assertTrue(inMemoryHistoryManager.getTasks().getFirst().equals(taskTwo));
        Assertions.assertTrue(inMemoryHistoryManager.getTasks().getLast().equals(taskThree));
        Assertions.assertTrue(taskFour.getDescription().equals("Новое описание"));
        Assertions.assertTrue(inMemoryHistoryManager.getTasks().size() == inMemoryHistoryManager.getNodes().size());
    }
}