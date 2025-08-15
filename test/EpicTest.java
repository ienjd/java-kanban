
import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;

import java.time.LocalDateTime;

class EpicTest {

    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
//экземпляры Epic равны друг другу, если равны их id
    void epicEqualsEpic() {
        ;
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Epic secondTask = firstEpic;

        Assertions.assertEquals(firstEpic.getId(), secondTask.getId());
        Assertions.assertEquals(firstEpic, secondTask);
    }

    @Test
    void epicDurationEqualDurationsAllSubtasksThisEpicAndEpicStartTimeEqualEarlierStartTimeThisEpicSubtasks()
             throws ManagerSaveException {
        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 4);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 4);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);


        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);


        Assertions.assertEquals(subtask1.getDuration().plus(subtask2.getDuration()), epic1.getEpicDuration());
        Assertions.assertEquals(epic1.getStartTime(), subtask1.getStartTime());

    }

}