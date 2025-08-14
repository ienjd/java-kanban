
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
//import ui.MainUI;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    Task task1 = inMemoryTaskManager.createTask("er", "er");
    task1.setDuration(15);
    task1.setStartTime(LocalDateTime.of(2025, 8,11, 14, 30));
    inMemoryTaskManager.addTaskToList(task1, inMemoryTaskManager.taskList);


    Task task2 = inMemoryTaskManager.createTask("er1", "er1");
    task2.setDuration(15);
    task2.setStartTime(LocalDateTime.of(2025, 8,11, 14, 30));
    inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.taskList);


    Task task3 = inMemoryTaskManager.createTask("er2", "er2");
    task3.setDuration(15);
    task3.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 46));
    inMemoryTaskManager.addTaskToList(task3, inMemoryTaskManager.taskList);


    Task task4 = inMemoryTaskManager.createTask("er3", "er3");
    task4.setDuration(15);
    task4.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 44));
    inMemoryTaskManager.addTaskToList(task4, inMemoryTaskManager.taskList);


    Task task5 = inMemoryTaskManager.createTask("er4", "er4");
    task5.setDuration(15);
    task5.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 44));
    inMemoryTaskManager.addTaskToList(task5, inMemoryTaskManager.taskList);


    Subtask subtask1 = inMemoryTaskManager.createSubtask("SS", "ss", 7);
    subtask1.setDuration(15);
    subtask1.setStartTime(LocalDateTime.of(2025, 8, 11, 20, 30));
    inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);


    Epic epic1 = inMemoryTaskManager.createEpic("EE", "ee");
    inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

    System.out.println(inMemoryTaskManager.getPrioritizedTasks());

        for (Object prioritizedTask : inMemoryTaskManager.getPrioritizedTasks()) {
            System.out.println(prioritizedTask);
        }
    /*







    System.out.println(inMemoryTaskManager.getPrioritizedTasks());
    */
    }

}






