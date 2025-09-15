import main.HttpTaskServer;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HandlerForHistoryViewTest {
    HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(new InMemoryTaskManager());

    @BeforeEach
    public void startServer() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        httpTaskServer.taskManager.idCount = 0;
        httpTaskServer.stop();
    }
    @Test
    public void handlerReturnHistoryList() throws IOException, InterruptedException {

        Task task = httpTaskServer.taskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task, httpTaskServer.taskManager.taskList);

        Task task2 = httpTaskServer.taskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        httpTaskServer.taskManager.addTaskToList(task2, httpTaskServer.taskManager.taskList);

        httpTaskServer.taskManager.findTask(task2.getId());
        httpTaskServer.taskManager.findTask(task.getId());

        URI uri = URI.create("http://localhost:8080/history");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        int code = response.statusCode();
        System.out.println(httpTaskServer.taskManager.getHistory());

        assertEquals(200, code);
        assertTrue(httpTaskServer.taskManager.getHistory().size() == 2);
        assertEquals(httpTaskServer.taskManager.getHistory().getFirst(), task2);
        assertEquals(httpTaskServer.taskManager.getHistory().getLast(), task);
    }
}
