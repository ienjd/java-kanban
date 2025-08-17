import manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;

class FileBackedTaskManagerTest {
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");

    @AfterEach
    void tearDown() {
    }
}