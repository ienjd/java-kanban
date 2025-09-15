import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForTasks;
import main.HttpTaskServer;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HandlerForTaskTest {

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
        Task task = httpTaskServer.taskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task, httpTaskServer.taskManager.taskList);
        URI uri = URI.create("http://localhost:8080/tasks");

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

        System.out.println(response.body());

        assertEquals(200, code);
        assertTrue(!httpTaskServer.taskManager.taskList.values().isEmpty());
    }

    @Test
    public void handlerReturnTask() throws IOException, InterruptedException {

        URI uri = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\n" +
                        "  \"title\": \"er\",\n" +
                        "  \"description\": \"er\",\n" +
                        "  \"id\": 1,\n" +
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
        assertNotNull(httpTaskServer.taskManager.findTask(1));
    }

    @Test
    public void handlerReturnCorrectTask() throws IOException, InterruptedException {

        Task task = httpTaskServer.taskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task, httpTaskServer.taskManager.taskList);

        Task task2 = httpTaskServer.taskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 10, 20, 17, 0));
        httpTaskServer.taskManager.addTaskToList(task2, httpTaskServer.taskManager.taskList);

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

        assertEquals(httpTaskServer.gson.toJson(task), task1);
        assertEquals(task, httpTaskServer.gson.fromJson(response.body(), Task.class));
        assertEquals(201, code);
    }


    @Test
    public void handlerDeleteCorrectTask() throws IOException, InterruptedException {

        Task task = httpTaskServer.taskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task, httpTaskServer.taskManager.taskList);

        assertTrue(httpTaskServer.taskManager.taskList.containsValue(task));

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
        System.out.println();

        assertFalse(httpTaskServer.taskManager.taskList.containsValue(task));
    }
}