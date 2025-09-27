import exceptions.ManagerSaveException;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager();

    @BeforeEach
    public void cleanAll() {
        fileBackedTaskManager1.deleteAllTasks();
        fileBackedTaskManager1.setIdCount(0);
        fileBackedTaskManager1.file = null;
    }

    @Test
    public void testException() {
        Assertions.assertThrows(NullPointerException.class, () ->
                        fileBackedTaskManager1.save(),
                "Ошибка сохранения");
    }

    @Test
    public void fileBackedTaskManagerGetEmptyFile() throws ManagerSaveException {
        fileBackedTaskManager1.file = new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        fileBackedTaskManager1.save();
        fileBackedTaskManager1.loadingFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));

        Assertions.assertTrue(Files.exists(Path.of("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv")));
        Assertions.assertTrue(fileBackedTaskManager1.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void createEpic() throws ManagerSaveException {
        fileBackedTaskManager1.file = new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        Epic epic1 = fileBackedTaskManager1.createEpic("ER", "er");
        fileBackedTaskManager1.setEpicDuration(1);
        fileBackedTaskManager1.setEpicStartTime(1);
        fileBackedTaskManager1.addTaskToList(epic1, fileBackedTaskManager1.getEpicList());
        fileBackedTaskManager1.save();


        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));
        Assertions.assertEquals(epic1, fileBackedTaskManager2.getPrioritizedTasks().getFirst());
    }

    @Test
    public void fileBackedTaskManagerGetHistory() throws ManagerSaveException {
        fileBackedTaskManager1.file = new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        Epic epic1 = fileBackedTaskManager1.createEpic("ER", "er");
        fileBackedTaskManager1.setEpicDuration(1);
        fileBackedTaskManager1.setEpicStartTime(1);
        fileBackedTaskManager1.addTaskToList(epic1, fileBackedTaskManager1.getEpicList());
        fileBackedTaskManager1.findTask(epic1.getId());

        Assertions.assertTrue(fileBackedTaskManager1.getHistory().contains(epic1));
    }

    @Test
    public void fileBackedTaskManagerGetEmptyHistory() throws IOException {
        fileBackedTaskManager1.file = new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        Epic epic1 = fileBackedTaskManager1.createEpic("ER", "er");
        fileBackedTaskManager1.setEpicDuration(1);
        fileBackedTaskManager1.setEpicStartTime(1);
        fileBackedTaskManager1.addTaskToList(epic1, fileBackedTaskManager1.getEpicList());
        fileBackedTaskManager1.findTask(epic1.getId());
        fileBackedTaskManager1.deleteTaskFromList(epic1.getId());

        Assertions.assertFalse(fileBackedTaskManager1.getHistory().contains(epic1));
    }
}