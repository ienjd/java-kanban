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
    void previousVersionOfTaskAddedToList() throws CloneNotSupportedException {
        Task task = inMemoryTaskManager.createTask("Первоначальный заголовок", "Первоначальное описание");

        inMemoryHistoryManager.add(task);

        task.setDescription("Обновленное описание");

        inMemoryHistoryManager.add(task);

        Assertions.assertNotEquals(inMemoryHistoryManager.getViewHistory().get(0).getDescription(),
                inMemoryHistoryManager.getViewHistory().get(1).getDescription());
    }

}