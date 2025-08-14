
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
//import ui.MainUI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        FileBackedTaskManager fileBackedTaskManager1 = new FileBackedTaskManager("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv");


        Task task1 = fileBackedTaskManager1.createTask("er", "er");
        task1.setDuration(15);
        task1.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 30));
        fileBackedTaskManager1.addTaskToList(task1, fileBackedTaskManager1.taskList);


        Task task2 = fileBackedTaskManager1.createTask("er1", "er1");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 30));
        fileBackedTaskManager1.addTaskToList(task2, fileBackedTaskManager1.taskList);


        Task task3 = fileBackedTaskManager1.createTask("er2", "er2");
        task3.setDuration(15);
        task3.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 46));
        fileBackedTaskManager1.addTaskToList(task3, fileBackedTaskManager1.taskList);


        Task task4 = fileBackedTaskManager1.createTask("er3", "er3");
        task4.setDuration(15);
        task4.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 44));
        fileBackedTaskManager1.addTaskToList(task4, fileBackedTaskManager1.taskList);


        Task task5 = fileBackedTaskManager1.createTask("er4", "er4");
        task5.setDuration(15);
        task5.setStartTime(LocalDateTime.of(2025, 8, 11, 14, 44));
        fileBackedTaskManager1.addTaskToList(task5, fileBackedTaskManager1.taskList);


        Subtask subtask1 = fileBackedTaskManager1.createSubtask("SS", "ss", 7);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2025, 8, 11, 20, 30));
        fileBackedTaskManager1.addTaskToList(subtask1, fileBackedTaskManager1.subtaskList);


        Epic epic1 = fileBackedTaskManager1.createEpic("EE", "ee");
        fileBackedTaskManager1.addTaskToList(epic1, fileBackedTaskManager1.epicList);

        fileBackedTaskManager1.save();
        FileBackedTaskManager.loadFromFile(new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));

        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(
                new File("C:\\Users\\coldh\\IdeaProjects\\java-kanban\\fileForSavingTasks\\file.csv"));

        System.out.println(fileBackedTaskManager1.getPrioritizedTasks());

    }
}






