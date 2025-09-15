package main;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import handlers.*;
import manager.InMemoryTaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer<T extends InMemoryTaskManager> {

    public static final int PORT = 8080;

    public final T taskManager;

    public HttpServer httpServer;

    public Gson gson = new GsonBuilder()
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

        HttpTaskServer http = new HttpTaskServer(new InMemoryTaskManager());
        http.start();
        System.out.println("Server started on 8080");
    }
}






