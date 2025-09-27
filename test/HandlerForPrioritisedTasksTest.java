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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandlerForPrioritisedTasksTest {

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
    public void handlerReturnPrioritizedList() throws IOException, InterruptedException {

        Task task = manager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        manager.addTaskToList(task, manager.getTaskList());

        Task task2 = manager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        manager.addTaskToList(task2, manager.getTaskList());

        Epic epic1 = manager.createEpic("er", "er");
        manager.setEpicStartTime(epic1.getId());
        manager.setEpicDuration(epic1.getId());
        manager.addTaskToList(epic1, manager.getEpicList());

        Subtask subtask1 = manager.createSubtask("er", "er", 3);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 45));
        manager.addTaskToList(subtask1, manager.getSubtaskList());


        URI uri = URI.create("http://localhost:8080/prioritized");

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
        assertTrue(manager.getPrioritizedTasks().size() == 3);
        assertEquals(manager.getPrioritizedTasks().getFirst(), epic1);
        assertEquals(manager.getPrioritizedTasks().getLast(), task2);

        LocalDateTime earlierDate = epic1.getStartTime();
        LocalDateTime laterDate = task2.getStartTime();

        assertEquals(true, earlierDate.isBefore(laterDate));
    }
}

