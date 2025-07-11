package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test// Данный метод проверяет сохранение предыдущей версии Task
    void previousVersionOfTaskAddedToList() {
        Task task = inMemoryTaskManager.createTask("Первоначальный заголовок", "Первоначальное описание");

        inMemoryHistoryManager.linkLast(task);

        task.setDescription("Обновленное описание");

        inMemoryHistoryManager.linkLast(task);

        Assertions.assertNotEquals(inMemoryHistoryManager.getTasks().get(0),
                inMemoryHistoryManager.getTasks().get(1));
    }

}