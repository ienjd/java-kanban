import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import tasks.Status;

import java.io.File;
import java.io.IOException;

public class Main {
    public void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        fileBackedTaskManager.createFileForSaving();
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createTask("er", "er")), fileBackedTaskManager.taskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createTask("er2", "er2")), fileBackedTaskManager.taskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("er3", "er3")), fileBackedTaskManager.epicList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("er4", "er4")), fileBackedTaskManager.epicList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("er5", "er5")), fileBackedTaskManager.epicList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("er6", "er6")), fileBackedTaskManager.epicList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createSubtask("er7", "er7", 3)), fileBackedTaskManager.subtaskList);
        System.out.println("Тесты практикума");
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createTask("Задача 1", "Описание задачи 1")),
                fileBackedTaskManager.taskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createTask("Задача 2", "Описание задачи 2")),
                fileBackedTaskManager.taskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("Эпик 1", "Описание эпика 1")),
                fileBackedTaskManager.epicList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createSubtask("Подзадача 1", "Описание подзадачи 1", 3)),
                fileBackedTaskManager.subtaskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createSubtask("Подзадача 2", "Описание подзадачи 2", 3)),
                fileBackedTaskManager.subtaskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createSubtask("Подзадача 3", "Описание подзадачи 3", 3)),
                fileBackedTaskManager.subtaskList);
        fileBackedTaskManager.addTaskToList((fileBackedTaskManager.createEpic("Эпик 2", "Описание эпика 2")),
                fileBackedTaskManager.epicList);
        for (Object task : fileBackedTaskManager.taskList.values()) {
            System.out.println(task);
        }
        for (Object task : fileBackedTaskManager.epicList.values()) {
            System.out.println(task);
        }
        for (Object task : fileBackedTaskManager.subtaskList.values()) {
            System.out.println(task);
        }
        System.out.println("_____________________________________________________________________________");
        fileBackedTaskManager.findTask(1);
        fileBackedTaskManager.findTask(2);
        fileBackedTaskManager.findTask(7);
        fileBackedTaskManager.findTask(3);
        fileBackedTaskManager.findTask(5);
        fileBackedTaskManager.findTask(4);
        fileBackedTaskManager.findTask(3);
        fileBackedTaskManager.findTask(7);
        fileBackedTaskManager.findTask(6);
        fileBackedTaskManager.findTask(2);
        fileBackedTaskManager.findTask(4);
        fileBackedTaskManager.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
        fileBackedTaskManager.findTask(7);
        fileBackedTaskManager.findTask(4);
        fileBackedTaskManager.findTask(6);
        fileBackedTaskManager.findTask(5);
        fileBackedTaskManager.findTask(7);
        fileBackedTaskManager.findTask(2);
        fileBackedTaskManager.findTask(1);
        fileBackedTaskManager.findTask(4);
        fileBackedTaskManager.findTask(7);
        fileBackedTaskManager.findTask(2);
        fileBackedTaskManager.findTask(1);
        fileBackedTaskManager.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
        fileBackedTaskManager.deleteTaskFromList(1);
        fileBackedTaskManager.getHistory().forEach(task -> System.out.println(task));
        System.out.println("_____________________________________________________________________________");
        fileBackedTaskManager.save();
    }
}





