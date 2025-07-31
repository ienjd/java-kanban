
import manager.FileBackedTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    public void main(String[] args) throws IOException {

        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager(
                "C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");
        System.out.println("Загружено из fileBackedTaskManager1");
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 1", "Описание задачи 1")),
                fileBackedTaskManager1.taskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 2", "Описание задачи 2")),
                fileBackedTaskManager1.taskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createEpic("Эпик 1", "Описание эпика 1")),
                fileBackedTaskManager1.epicList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 1", "Описание подзадачи 1", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 2", "Описание подзадачи 2", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createSubtask("Подзадача 3", "Описание подзадачи 3", 3)),
                fileBackedTaskManager1.subtaskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createEpic("Эпик 2", "Описание эпика 2")),
                fileBackedTaskManager1.epicList);
        for (Task value : fileBackedTaskManager1.taskList.values()) {
            System.out.println(value);
        }
        for (Epic value : fileBackedTaskManager1.epicList.values()) {
            System.out.println(value);
        }
        for (Subtask value : fileBackedTaskManager1.subtaskList.values()) {
            System.out.println(value);
        }

        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 666", "Описание задачи 666")),
                fileBackedTaskManager1.taskList);
        fileBackedTaskManager1.addTaskToList((fileBackedTaskManager1.createTask("Задача 666", "Описание задачи 667")),
                fileBackedTaskManager1.taskList);

        System.out.println("добавлены новые задачи, выгружены в fileBackedTaskManager2 из уже имеющегося файла");

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));

        for (Task value : fileBackedTaskManager2.taskList.values()) {
            System.out.println(value);
        }
        for (Epic value : fileBackedTaskManager2.epicList.values()) {
            System.out.println(value);
        }
        for (Subtask value : fileBackedTaskManager2.subtaskList.values()) {
            System.out.println(value);
        }
    }

}






