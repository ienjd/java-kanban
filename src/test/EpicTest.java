package test;

import manager.InMemoryTaskManager;
import tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test//экземпляры Epic равны друг другу, если равны их id
    void EpicEqualsEpic() {
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Epic secondTask = firstEpic;

        Assertions.assertEquals(firstEpic.getId(), secondTask.getId());
        Assertions.assertEquals(firstEpic, secondTask);
    }

}