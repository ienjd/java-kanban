import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForSubtasks;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
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

class HandlerForSubtaskTest {

    @BeforeEach
    public void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/subtasks", new HandlerForSubtasks());

        httpServer.start();

        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.epicList.clear();
        InMemoryTaskManager.idCount = 0;
    }

    @AfterEach
    public void stopServer() {
        httpServer.stop(0);
    }

    @Test
    public void HandlerReturnCode200() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/subtasks");

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
    public void HandlerReturnSubtask() throws IOException, InterruptedException {
        Epic epic1 = inMemoryTaskManager.createEpic("er", "er");
        inMemoryTaskManager.setEpicStartTime(epic1.getId());
        inMemoryTaskManager.setEpicDuration(epic1.getId());
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        URI uri = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                        "  \"title\": \"er\",\n" +
                        "  \"description\": \"er\",\n" +
                        "  \"id\": 2,\n" +
                        "  \"epicId\": 1,\n" +
                        "  \"status\": \"NEW\",\n" +
                        "  \"duration\": \"00:15:00\",\n" +
                        "  \"startTime\": \"20--10--2025 20:00:00\"\n" +
                        "}"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("Accept",  "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();

        assertEquals(201, code);
    }

    @Test
    public void HandlerReturnCorrectTask() throws IOException, InterruptedException {
        Epic epic1 = inMemoryTaskManager.createEpic("er", "er");
        inMemoryTaskManager.setEpicStartTime(epic1.getId());
        inMemoryTaskManager.setEpicDuration(epic1.getId());
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("er", "er", 1);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);

        Subtask subtask2 = inMemoryTaskManager.createSubtask("er", "er", 1);
        subtask2.setDuration(15);
        subtask2.setStartTime(LocalDateTime.of(2025, 10, 20, 17, 0));
        inMemoryTaskManager.addTaskToList(subtask2, inMemoryTaskManager.subtaskList);

        URI uri = URI.create("http://localhost:8080/subtasks/3");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        String subtask = response.body();
        int code = response.statusCode();

        assertEquals(gson.toJson(subtask2), subtask);
        assertEquals(subtask2, gson.fromJson(response.body(), Subtask.class));
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