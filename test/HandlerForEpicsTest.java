import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForEpics;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static main.HttpTaskServer.*;
import static main.HttpTaskServer.gson;
import static main.HttpTaskServer.httpServer;
import static main.HttpTaskServer.inMemoryTaskManager;
import static org.junit.jupiter.api.Assertions.*;

class HandlerForEpicsTest {

    @BeforeEach
    public void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/epics", new HandlerForEpics());

        httpServer.start();

        inMemoryTaskManager.taskList.clear();
        inMemoryTaskManager.subtaskList.clear();
        inMemoryTaskManager.epicList.clear();
        InMemoryTaskManager.idCount = 0;
    }

    @AfterEach
    public void stopServer(){
        httpServer.stop(0);
    }

    @Test
    public void handlerReturnCode200() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("Accept",  "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();

        assertEquals(200, code);
    }

    @Test
    public void handlerReturnEpic() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                        "  \"title\": \"er\",\n" +
                        "  \"description\": \"er\",\n" +
                        "  \"id\": 1,\n" +
                        "  \"status\": \"NEW\",\n" +
                        "  \"duration\": \"00:15:00\",\n" +
                        "  \"startTime\": \"10--08--2024 16:15:00\"\n" +
                        "}"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();

        assertEquals(201, code);
    }

    @Test
    public void handlerReturnCorrectEpic() throws IOException, InterruptedException {

        Epic task = inMemoryTaskManager.createEpic("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.epicList);

        Epic task2 = inMemoryTaskManager.createEpic("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 10, 20, 17, 0));
        inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.epicList);

        URI uri = URI.create("http://localhost:8080/epics/1");

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
        assertEquals(task, gson.fromJson(response.body(), Epic.class));
        assertEquals(201, code);
    }


    @Test
    public void handlerDeleteCorrectTask() throws IOException, InterruptedException {

        Epic task = inMemoryTaskManager.createEpic("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.epicList);

        assertTrue(inMemoryTaskManager.epicList.containsValue(task));

        URI uri = URI.create("http://localhost:8080/epics/1");

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

        assertFalse(inMemoryTaskManager.epicList.containsValue(task));
    }
}