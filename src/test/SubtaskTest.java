package test;

import InMemoryTaskManager.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test//экземпляры Subtask равны друг другу, если равны их id
    void createSubtask() {
        Subtask firstSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", 1);
        Subtask secondSubtask = firstSubtask;

        Assertions.assertEquals(firstSubtask.getId(), secondSubtask.getId());
        Assertions.assertEquals(firstSubtask, secondSubtask);
    }

    @Test// объект Subtask нельзя сделать своим эпиком
    void createSubtaskWithEpicIdEqualsSubtaskId() {
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Subtask thirdSubtask = inMemoryTaskManager.createSubtask("Первая подзадача", "Описание первой подзадачи", firstEpic.getId());
        Subtask fourthSubtask = thirdSubtask;
        fourthSubtask.setEpicId(2);
        Assertions.assertNotEquals(2, fourthSubtask.getEpicId());
    }
}