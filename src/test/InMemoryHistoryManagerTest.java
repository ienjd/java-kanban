package test;

import HistoryManager.InMemoryHistoryManager;
import InMemoryTaskManager.InMemoryTaskManager;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test// Данный метод проверяет сохранение предыдущей версии Task
    void previousVersionOfTaskAddedToList() throws CloneNotSupportedException {
        Task task = inMemoryTaskManager.createSubtask("Первоначальный заголовок", "Первоначальное описание", 2);

        inMemoryHistoryManager.add(task);

        task.setDescription("Обновленное описание");

        inMemoryHistoryManager.add(task);

        Assertions.assertEquals(inMemoryHistoryManager.getHistory().get(0), inMemoryHistoryManager.getHistory().get(1));
    }

}