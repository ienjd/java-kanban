import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

class FileBackedTaskManagerTest{
    FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager
            (new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));


    @Test
    void fileIsExist(){
        Assertions.assertTrue(fileBackedTaskManager.file.exists());
    }

    @Test
    public void testException() {
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager();
        Assertions.assertThrows(NullPointerException.class, () -> {
            fileBackedTaskManager1.save();
        }, "Ошибка сохранения");
    }

}