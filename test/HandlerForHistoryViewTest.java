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

class HandlerForHistoryViewTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(manager);

    @BeforeEach
    public void startServer() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    public void stopServer() {
        manager.setIdCount(0);
        httpTaskServer.stop();
    }
    @Test
    public void handlerReturnHistoryList() throws IOException, InterruptedException {

        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());

        Task task2 = manager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        manager.addTaskToList(task2, manager.getTaskList());

        manager.findTask(task2.getId());
        manager.findTask(task.getId());

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

        assertEquals(200, code);
        assertTrue(manager.getHistory().size() == 2);
        assertEquals(manager.getHistory().getFirst(), task2);
        assertEquals(manager.getHistory().getLast(), task);
    }
}
