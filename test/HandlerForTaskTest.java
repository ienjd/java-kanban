import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForTasks;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static main.HttpTaskServer.*;
import static org.junit.jupiter.api.Assertions.*;

class HandlerForTaskTest {

    @BeforeEach
    public void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new HandlerForTasks());

        httpServer.start();

        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.epicList.clear();
        InMemoryTaskManager.idCount = 0;
    }

    @AfterEach
    public void stopServer(){
        httpServer.stop(2);
    }

    @Test
    public void HandlerReturnCode200() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();

        assertEquals(200, code);
    }

    @Test
    public void HandlerReturnCorrectTask() throws IOException, InterruptedException {

        Task task = inMemoryTaskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Task task2 = inMemoryTaskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 10, 20, 17, 0));
        inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.taskList);

        URI uri = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        String task1 = response.body();
        int code = response.statusCode();

        assertEquals(gson.toJson(task), task1);
        assertEquals(task, gson.fromJson(response.body(), Task.class));
        assertEquals(201, code);
    }


    @Test
    public void HandlerDeleteCorrectTask() throws IOException, InterruptedException {

        Task task = inMemoryTaskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        assertTrue(inMemoryTaskManager.taskList.containsValue(task));

        URI uri = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        assertFalse(inMemoryTaskManager.taskList.containsValue(task));
    }

}