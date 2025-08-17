import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

class EpicStatusTestDone {

    @Test
    void epicStatusDoneCalculatesBasedOnStatusesEpicSubtasksDone() throws ManagerSaveException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 6);
        subtask3.setStatus(Status.DONE);
        inMemoryTaskManager.subtaskList.put(subtask3.getId(), subtask3);


        Subtask subtask4 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 6);
        subtask4.setStatus(Status.DONE);
        inMemoryTaskManager.subtaskList.put(subtask4.getId(), subtask4);


        Epic epic2 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.updateEpicStatus(epic2);
        inMemoryTaskManager.epicList.put(epic2.getId(), epic2);

        Assertions.assertEquals(Status.DONE, epic2.getStatus());
    }
}