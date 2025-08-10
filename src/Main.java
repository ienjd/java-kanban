
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import ui.MainUI;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    inMemoryTaskManager.addTaskToList(inMemoryTaskManager.createEpic("er", "er"), inMemoryTaskManager.epicList);
        System.out.println(inMemoryTaskManager.epicList.get(1));

    inMemoryTaskManager.addTaskToList(inMemoryTaskManager.createSubtask("er1", "er1", 1),
            inMemoryTaskManager.subtaskList);
    inMemoryTaskManager.subtaskList.get(2).setDuration(15);
    System.out.println(inMemoryTaskManager.subtaskList.get(2).getDuration());

    inMemoryTaskManager.addTaskToList(inMemoryTaskManager.createSubtask("er2", "er2", 1),
                inMemoryTaskManager.subtaskList);
    inMemoryTaskManager.subtaskList.get(3).setDuration(15);

        inMemoryTaskManager.addTaskToList(inMemoryTaskManager.createSubtask("er3", "er3", 1),
                inMemoryTaskManager.subtaskList);
        inMemoryTaskManager.subtaskList.get(4).setDuration(15);

    inMemoryTaskManager.setEpicDuration(1);
    }
}






