package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;

class SubtaskTest {

    @Test//экземпляры Subtask равны друг другу, если равны их id, а также, объект Subtask нельзя сделать своим эпиком
    void SubtaskEqualsSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        Subtask secondSubtask = firstSubtask;

        Assertions.assertEquals(firstSubtask.getId(), secondSubtask.getId());
        Assertions.assertEquals(firstSubtask, secondSubtask);

        secondSubtask.setEpicId(1);
        Assertions.assertNotEquals(1, secondSubtask.getEpicId());
    }

}