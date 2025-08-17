
import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Subtask;

import java.time.LocalDateTime;

class EpicTest {

    public InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();


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


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 4);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Epic secondTask = firstEpic;
        // проверяется идентичность эпиков
        Assertions.assertEquals(firstEpic.getId(), secondTask.getId());
        Assertions.assertEquals(firstEpic, secondTask);

        // проверяется равенство продолжительности эпика сумме продолжительностей его эпиков
        Assertions.assertEquals(subtask1.getDuration().plus(subtask2.getDuration()), epic1.getEpicDuration());
        Assertions.assertEquals(epic1.getStartTime(), subtask1.getStartTime());
        // проверяется невключение подзадачи, пересекающейся по времени выполнения с другой подзадачей
        // в приоритезированный список
        Assertions.assertTrue(!inMemoryTaskManager.getPrioritizedTasks().contains(subtask3));
    }
}
