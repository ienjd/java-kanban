import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ManagerSaveException;
import main.HttpTaskServer;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandlerForEpicsTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    HttpTaskServer<InMemoryTaskManager> httpTaskServer = new HttpTaskServer<>(manager);
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

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
    public void handlerReturnCode200AndEpicList() throws IOException, InterruptedException {

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
        System.out.println(response.body());

        assertEquals(200, code);
        assertTrue( manager.getEpicList().size() == 0);
        assertTrue(response.body().contains("[]"));
    }

    @Test
    public void handlerReturnCorrectEpic() throws IOException, InterruptedException {
        Epic epic1 = manager.createEpic("er", "er");
        epic1.setDuration(15);
        epic1.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));

        Epic epic2 = manager.createEpic("er", "er");
        epic2.setDuration(15);
        epic2.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));

        URI uri = URI.create("http://localhost:8080/epics/1");

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

        assertTrue(response.body().equals(gson.toJson(epic1)));
        assertFalse(response.body().equals(gson.toJson(epic2)));

    }

    @Test
    public void handlerCreateEpic() throws IOException, InterruptedException {

        assertTrue(manager.getEpicList().isEmpty());
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
        assertFalse(manager.getEpicList().isEmpty());
        assertEquals(201, code);
        assertTrue(manager.getEpicList().values().contains(manager.findTask(1)));
    }

    @Test
    public void handlerDeleteCorrectTask() throws IOException, InterruptedException {

        Epic task = manager.createEpic("er", "er");
        task.setDuration(15);
        task.setStartTime(LocalDateTime.of(2025, 10, 20, 15, 0));

        assertTrue(manager.getEpicList().containsValue(task));

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

        assertFalse(manager.getEpicList().containsValue(task));
    }
}
