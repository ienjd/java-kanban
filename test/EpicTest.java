import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.time.LocalDateTime;

public class EpicTest {

    public InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    public void cleanAll() {
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.epicList.clear();
        inMemoryTaskManager.idCount = 0;
    }

    @Test
    public void epicEqualsEpic() throws ManagerSaveException {
        Epic firstEpic = inMemoryTaskManager.createEpic("Первый эпик", "Описание первого эпика");
        Epic secondTask = firstEpic;
        // проверяется идентичность эпиков
        Assertions.assertEquals(firstEpic.getId(), secondTask.getId());
        Assertions.assertEquals(firstEpic, secondTask);
    }

    @Test
    public void epicDurationEqualDurationsAllSubtasksThisEpicAndEpicStartTimeEqualEarlierStartTimeThisEpicSubtasks()
            throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 1));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);




        // проверяется равенство продолжительности эпика сумме продолжительностей его эпиков
        Assertions.assertEquals(subtask1.getDuration().plus(subtask2.getDuration()), epic1.getEpicDuration());
        Assertions.assertEquals(epic1.getStartTime(), subtask1.getStartTime());
        // проверяется невключение подзадачи, пересекающейся по времени выполнения с другой подзадачей
        // в приоритезированный список

    }

    @Test
    public void prioretizedTaskListNotContainsTaskWithIntersectsTimeLapses() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);


        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        Assertions.assertFalse(inMemoryTaskManager.getPrioritizedTasks().contains(subtask3));
    }

    @Test
    public void epicStatusInProgressCalculatesBasedOnStatusesEpicSubtasksInProgress() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.setEpicDuration(1);
        inMemoryTaskManager.setEpicStartTime(1);
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);

        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        inMemoryTaskManager.updateEpicStatus(epic1);


        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void epicStatusDoneCalculatesBasedOnStatusesEpicSubtasksDone() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.setEpicDuration(1);
        inMemoryTaskManager.setEpicStartTime(1);
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setStatus(Status.DONE);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setStatus(Status.DONE);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 45));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        inMemoryTaskManager.updateEpicStatus(epic1);


        Assertions.assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    public void epicStatusInProgressCalculatesBasedOnStatusesEpicSubtasksDoneAndNew() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.setEpicDuration(1);
        inMemoryTaskManager.setEpicStartTime(1);
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setStatus(Status.DONE);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setStatus(Status.NEW);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 50));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        inMemoryTaskManager.updateEpicStatus(epic1);

        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void epicStatusNewCalculatesBasedOnStatusesEpicSubtasksNew() throws ManagerSaveException {

        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.setEpicDuration(1);
        inMemoryTaskManager.setEpicStartTime(1);

        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 1);
        subtask1.setStatus(Status.NEW);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 1);
        subtask2.setStatus(Status.NEW);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 20));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        inMemoryTaskManager.updateEpicStatus(epic1);


        Assertions.assertEquals(Status.NEW, epic1.getStatus());
    }
}
