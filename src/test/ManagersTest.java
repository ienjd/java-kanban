package test;

import InMemoryTaskManager.InMemoryTaskManager;
import InMemoryTaskManager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test//Метод getDefault класса Manager возвращает объект TaskManager
    void getDefault() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test//Метод getDefaultHistory класса Manager возвращает объект HistoryManager
    void getDefaultHistory() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}