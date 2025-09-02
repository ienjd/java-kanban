package main;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForEpics;
import handlers.HandlerForHistoryView;
import handlers.HandlerForPrioritisedTasks;
import handlers.HandlerForTasks;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 3);
        httpServer.createContext("/prioritized", new HandlerForPrioritisedTasks());
        httpServer.createContext("/tasks", new HandlerForTasks());
        httpServer.createContext("/epics", new HandlerForEpics());
        httpServer.createContext("/history", new HandlerForHistoryView());
        httpServer.start();
        System.out.println("взлет");

        Task task = inMemoryTaskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Task task2 = inMemoryTaskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.taskList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("er", "er", 5);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 45));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);

        Subtask subtask2 = inMemoryTaskManager.createSubtask("er", "er", 5);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2024, 8, 10, 16, 15));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        Epic epic1 = inMemoryTaskManager.createEpic("er", "er");
        inMemoryTaskManager.setEpicStartTime(epic1.getId());
        inMemoryTaskManager.setEpicDuration(epic1.getId());
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);
        epic1.fillSubtasks(inMemoryTaskManager.getEpicSubtasks(epic1.getId()));
        System.out.println(task);
        System.out.println(epic1);
        //System.out.println(gson.toJson(subtask1));
        System.out.println(gson.toJson(epic1));
    }
}






