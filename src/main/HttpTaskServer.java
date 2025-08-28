package main;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForHistoryView;
import handlers.HandlerForPrioritisedTasks;
import handlers.HandlerForTasks;
import manager.InMemoryTaskManager;
import tasks.Task;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

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
        httpServer.createContext("/history", new HandlerForHistoryView());
        httpServer.start();

        Task task = inMemoryTaskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Task task2 = inMemoryTaskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.taskList);

        String array = gson.toJson(inMemoryTaskManager.taskList.values());
        System.out.println(array);
        inMemoryTaskManager.findTask(2);

    }
}






