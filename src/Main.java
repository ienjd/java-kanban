import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Subtask subtask3 = inMemoryTaskManager.createSubtask("subtask2", "subtask2", 2);
        subtask3.setDuration(15);
        subtask3.setStartTime(LocalDateTime.of(2001, 1, 1, 1, 40));
        inMemoryTaskManager.addTaskToList(subtask3, inMemoryTaskManager.subtaskList);


        Epic epic1 = inMemoryTaskManager.createEpic("epic1", "epic1");
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);


        inMemoryTaskManager.updateSubtask(subtask3);

        Epic epic2 = (Epic) inMemoryTaskManager.findTask(2);
        inMemoryTaskManager.getEpicSubtasks(2);
        System.out.println(inMemoryTaskManager.findTask(1));
        System.out.println(epic2.getSubtasks());
    }
}






