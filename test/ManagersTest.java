import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
//Метод getDefault класса Manager возвращает объект TaskManager
    public void methodGetDefaultReturnObjectTaskManager() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
//Метод getDefaultHistory класса Manager возвращает объект HistoryManager
    public void methodGetDefaultHistoryReturnObjectHistoryManager() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}