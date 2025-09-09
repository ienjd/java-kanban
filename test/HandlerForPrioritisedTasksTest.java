import com.sun.net.httpserver.HttpServer;
import handlers.HandlerForEpics;
import handlers.HandlerForPrioritisedTasks;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

import static main.HttpTaskServer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandlerForPrioritisedTasksTest {

    @Test
    public void handlerReturnPrioritizedList() throws IOException, InterruptedException {

        Task task = inMemoryTaskManager.createTask("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task, inMemoryTaskManager.taskList);

        Task task2 = inMemoryTaskManager.createTask("er", "er");
        task2.setDuration(15);
        task2.setStartTime(LocalDateTime.of(2025, 11, 20, 15, 0));
        inMemoryTaskManager.addTaskToList(task2, inMemoryTaskManager.taskList);

        Epic epic1 = inMemoryTaskManager.createEpic("er", "er");
        inMemoryTaskManager.setEpicStartTime(epic1.getId());
        inMemoryTaskManager.setEpicDuration(epic1.getId());
        inMemoryTaskManager.addTaskToList(epic1, inMemoryTaskManager.epicList);

        Subtask subtask1 = inMemoryTaskManager.createSubtask("er", "er", 3);
        subtask1.setDuration(15);
        subtask1.setStartTime(LocalDateTime.of(2024, 8, 10, 15, 45));
        inMemoryTaskManager.addTaskToList(subtask1, inMemoryTaskManager.subtaskList);

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/prioritized", new HandlerForPrioritisedTasks());

        httpServer.start();

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
        assertTrue(inMemoryTaskManager.getPrioritizedTasks().size() == 3);
        assertEquals(inMemoryTaskManager.getPrioritizedTasks().getFirst(), epic1);
        assertEquals(inMemoryTaskManager.getPrioritizedTasks().getLast(), task2);

        LocalDateTime earlierDate = epic1.getStartTime();
        LocalDateTime laterDate = task2.getStartTime();

        assertEquals(true, earlierDate.isBefore(laterDate));
    }
}