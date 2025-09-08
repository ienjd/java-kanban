package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.IOException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForEpics extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("epics")) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.epicList.values()), 200);

            } else if (Arrays.stream(splitReq).toList().getLast().equals("subtasks")){
                sendText(exchange, gson.toJson(inMemoryTaskManager.getEpicSubtasks(Integer.parseInt(splitReq[splitReq.length - 2]))), 200);
            }

            if (inMemoryTaskManager.epicList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Epic newTask = gson.fromJson(body, Epic.class);

            if (inMemoryTaskManager.epicList.containsKey(newTask.getId())) {
                inMemoryTaskManager.updateEpicStatus((Epic) inMemoryTaskManager.findTask(newTask.getId()));
                sendText(exchange, gson.toJson(newTask), 201);
            } else if (!inMemoryTaskManager.epicList.containsKey(newTask.getId())) {
                if (inMemoryTaskManager.isOverLapping(newTask)) {
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                } else {
                    inMemoryTaskManager.addTaskToList(newTask, inMemoryTaskManager.epicList);
                    sendText(exchange, gson.toJson(newTask), 201);
                }
            }

        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.epicList.remove(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
