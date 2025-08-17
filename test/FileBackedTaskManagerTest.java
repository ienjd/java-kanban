import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;

class FileBackedTaskManagerTest{

    @Test
    void fileIsExist(){
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager
                (new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));
        Assertions.assertTrue(fileBackedTaskManager.file.exists());
    }

    @Test
    public void testException() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        Assertions.assertThrows(NullPointerException.class, () -> {
            fileBackedTaskManager.save();
        }, "Ошибка сохранения");
    }

}