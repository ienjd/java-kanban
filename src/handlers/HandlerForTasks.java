package handlers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.GsonBuilder;
import com.sun.jdi.Value;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import adapters.LocalDateTimeAdapter;
import adapters.DurationAdapter;
import manager.InMemoryTaskManager;
import tasks.Task;

import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForTasks extends BaseHttpHandler implements HttpHandler {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd--MM--yyyy HH:mm:ss");

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();


        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("tasks")) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.taskList.values()), 200);
            }

            if (inMemoryTaskManager.taskList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!");
            }

        } else if (reqMethod.equals("POST")) {

            String params = exchange.getRequestURI().getQuery();

            if (!(exchange.getRequestURI().getQuery().split("&"))[0].contains("id")) {


                String title = ((params.split("&"))[0]).split("=")[1];
                String descr = ((params.split("&"))[1]).split("=")[1];
                String duration = ((params.split("&"))[2]).split("=")[1];
                String startTime = ((params.split("&"))[3]).split("=")[1];

                Task newTask = inMemoryTaskManager.createTask(title, descr);
                newTask.setDuration(Integer.parseInt(duration));
                newTask.setStartTime(LocalDateTime.of(
                        Integer.parseInt(startTime.substring(6, 10)),
                        Integer.parseInt(startTime.substring(3, 5)),
                        Integer.parseInt(startTime.substring(0, 2)),
                        Integer.parseInt(startTime.substring(11, 13)),
                        Integer.parseInt(startTime.substring(14, 16))));

                inMemoryTaskManager.sortingTasks();

                if (inMemoryTaskManager.isOverLapping(newTask)) {
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими");
                    inMemoryTaskManager.idCount--;
                }

                inMemoryTaskManager.addTaskToList(newTask, inMemoryTaskManager.taskList);
                sendText(exchange, gson.toJson(newTask), 201);

            } else if ((exchange.getRequestURI().getQuery().split("&"))[0].contains("id")) {

                String id = ((params.split("&"))[0]).split("=")[1];
                String title = ((params.split("&"))[1]).split("=")[1];
                String descr = ((params.split("&"))[2]).split("=")[1];
                String duration = ((params.split("&"))[3]).split("=")[1];
                String startTime = ((params.split("&"))[4]).split("=")[1];

                Task updatedTask = inMemoryTaskManager.createTask(title, descr);
                updatedTask.setId(Integer.parseInt(id));
                updatedTask.setDuration(Integer.parseInt(duration));
                updatedTask.setStartTime(LocalDateTime.of(
                        Integer.parseInt(startTime.substring(6, 10)),
                        Integer.parseInt(startTime.substring(3, 5)),
                        Integer.parseInt(startTime.substring(0, 2)),
                        Integer.parseInt(startTime.substring(11, 13)),
                        Integer.parseInt(startTime.substring(14, 16))));

                inMemoryTaskManager.taskList.put(updatedTask.getId(), updatedTask);

                sendText(exchange, gson.toJson(updatedTask), 201);
            }
        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
