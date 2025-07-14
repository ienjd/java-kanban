
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;

class EpicTest {

    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
//экземпляры Epic равны друг другу, если равны их id
    void epicEqualsEpic() {
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Epic secondTask = firstEpic;

        Assertions.assertEquals(firstEpic.getId(), secondTask.getId());
        Assertions.assertEquals(firstEpic, secondTask);
    }

}