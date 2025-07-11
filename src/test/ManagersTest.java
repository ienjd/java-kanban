package test;

import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test//Метод getDefault класса Manager возвращает объект TaskManager
    void MethodGetDefaultReturnObjectTaskManager(){
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test//Метод getDefaultHistory класса Manager возвращает объект HistoryManager
    void MethodGetDefaultHistoryReturnObjectHistoryManager(){
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}