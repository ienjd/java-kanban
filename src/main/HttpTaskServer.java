package main;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer<T extends InMemoryTaskManager> {

    private static final int PORT = 8080;

    private final T taskManager;

    private HttpServer httpServer;

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskServer(T taskManager) {

        this.taskManager = taskManager;

        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registerContexts();
    }

    public String convertToJson(Task task) {
        return gson.toJson(task);
    }

    public Task convertFromJson(String string) {
        return gson.fromJson(string, Task.class);
    }

    private void registerContexts() {

        httpServer.createContext("/tasks", new HandlerForTasks(taskManager));
        httpServer.createContext("/epics", new HandlerForEpics(taskManager));
        httpServer.createContext("/subtasks", new HandlerForSubtasks(taskManager));
        httpServer.createContext("/history", new HandlerForHistoryView(taskManager));
        httpServer.createContext("/prioritized", new HandlerForPrioritisedTasks(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {


        InMemoryTaskManager manager = new InMemoryTaskManager();
        HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(manager);

        httpTaskServer.start();
        System.out.println("Server started on 8080");

        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());

        Task task2 = manager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        manager.addTaskToList(task2, manager.getTaskList());

        Epic epic1 = manager.createEpic("er", "er");
        manager.setEpicStartTime(epic1.getId());
        manager.setEpicDuration(epic1.getId());
        manager.addTaskToList(epic1, manager.getEpicList());

        Subtask subtask1 = manager.createSubtask("er", "er", 3);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 45));
        manager.addTaskToList(subtask1, manager.getSubtaskList());
        System.out.println(manager.getEpicList());
        manager.deleteTaskFromList(epic1.getId());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getTaskList());
        manager.deleteTaskFromList(task.getId());
        System.out.println(manager.getTaskList());
    }
}






