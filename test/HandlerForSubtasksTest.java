import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForSubtasks;
import main.HttpTaskServer;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HandlerForSubtaskTest {
    HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(new InMemoryTaskManager());

    @BeforeEach
    public void startServer() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer(){
        httpTaskServer.taskManager.idCount = 0;
        httpTaskServer.stop();
    }

    @Test
    public void handlerReturnCode200() throws IOException, InterruptedException {

        Epic task = httpTaskServer.taskManager.createEpic("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task, httpTaskServer.taskManager.epicList);

        Subtask subtask = httpTaskServer.taskManager.createSubtask("er", "er", 1);
        subtask.setDuration(15);
        subtask.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(subtask, httpTaskServer.taskManager.subtaskList);

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
    public void handlerReturnSubtask() throws IOException, InterruptedException {
        Epic epic1 = httpTaskServer.taskManager.createEpic("er", "er");
        httpTaskServer.taskManager.setEpicStartTime(epic1.getId());
        httpTaskServer.taskManager.setEpicDuration(epic1.getId());
        httpTaskServer.taskManager.addTaskToList(epic1,  httpTaskServer.taskManager.epicList);

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
    public void handlerDeleteCorrectTask() throws IOException, InterruptedException {

        Epic epic = httpTaskServer.taskManager.createEpic("er", "er");
        epic.setDuration(15);
        epic.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(epic,  httpTaskServer.taskManager.epicList);

        Subtask task =  httpTaskServer.taskManager.createSubtask("er", "er", 1);
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task,  httpTaskServer.taskManager.subtaskList);

        assertTrue(httpTaskServer.taskManager.subtaskList.containsValue(task));

        URI uri = URI.create("http://localhost:8080/subtasks/1");

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

        assertFalse(httpTaskServer.taskManager.taskList.containsValue(task));
    }
}
