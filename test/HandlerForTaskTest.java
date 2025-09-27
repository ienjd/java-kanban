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

    InMemoryTaskManager manager = new InMemoryTaskManager();
    HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(manager);

    @BeforeEach
    public void startServer() throws IOException {
       httpTaskServer.start();
    }

    @AfterEach
    public void stopServer(){
        manager.setIdCount(0);
        httpTaskServer.stop();
    }

    @Test
    public void handlerReturnCode200() throws IOException, InterruptedException {
        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());
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


        assertEquals(200, code);
        assertEquals(true, !manager.getTaskList().values().isEmpty());
        assertTrue( manager.getTaskList().size() != 0);
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
        assertNotNull(manager.findTask(1));
    }

    @Test
    public void handlerReturnCorrectTask() throws IOException, InterruptedException {

        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());

        Task task2 = manager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 10, 20, 17, 0));
        manager.addTaskToList(task2, manager.getTaskList());

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

        assertEquals(httpTaskServer.convertToJson(task), task1);
        assertEquals(task, httpTaskServer.convertFromJson(response.body()));
        assertEquals(201, code);
    }


    @Test
    public void handlerDeleteCorrectTask() throws IOException, InterruptedException {

        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());

        assertTrue(manager.getTaskList().containsValue(task));

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

        assertFalse(manager.getTaskList().containsValue(task));
    }
}