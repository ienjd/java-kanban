package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.GsonBuilder;
import com.sun.jdi.Value;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import com.google.gson.Gson;
import adapters.LocalDateTimeAdapter;
import adapters.DurationAdapter;
import manager.InMemoryTaskManager;

import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForTasks extends BaseHttpHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String [] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        String classifier;

        if (reqMethod.equals("GET")) {
            if (Arrays.stream(splitReq).toList().getLast().equals("tasks")) {
                classifier = "get tasks";
            } else {
                classifier = "get task by id";
            }
        } else if (reqMethod.equals("POST")) {
            classifier = "create task";
        } else {
            classifier = "delete task";
        }

        switch (classifier) {

            case "get tasks" -> sendText(exchange, gson.toJson(inMemoryTaskManager.taskList.values()));

            case "get task by id" -> {
                if (inMemoryTaskManager.taskList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))){
                    sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(
                            Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))));
                } else {
                    sendNotFound(exchange, "Задача с данным id не найдена");
                }
            }
        }
    }
}
