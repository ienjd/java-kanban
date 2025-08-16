import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

class EpicStatusTestAllSubtasksInProgress {
    static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @Test
    void epicStatusInProgressCalculatesBasedOnStatusesEpicSubtasksInProgress() throws ManagerSaveException {

        Subtask subtask1 = inMemoryTaskManager.createSubtask("subtask1", "subtask1", 3);
        subtask1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.subtaskList.put(subtask1.getId(), subtask1);


        Subtask subtask2 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 3);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.subtaskList.put(subtask2.getId(), subtask2);


        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.updateEpicStatus(epic1);
        inMemoryTaskManager.epicList.put(epic1.getId(), epic1);

        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }
}