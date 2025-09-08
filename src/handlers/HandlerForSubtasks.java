package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForSubtasks extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("subtasks")) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.subtaskList.values()), 200);
            }

            if (inMemoryTaskManager.subtaskList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Subtask newTask = gson.fromJson(body, Subtask.class);

            if (inMemoryTaskManager.subtaskList.containsKey(newTask.getId())) {
                inMemoryTaskManager.updateSubtask((Subtask) inMemoryTaskManager.findTask(newTask.getId()));
                sendText(exchange, gson.toJson(newTask), 201);
            } else if (!inMemoryTaskManager.taskList.containsKey(newTask.getId())) {
                if (inMemoryTaskManager.isOverLapping(newTask)) {
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                } else {
                    inMemoryTaskManager.addTaskToList(newTask, inMemoryTaskManager.subtaskList);
                    sendText(exchange, gson.toJson(newTask), 201);
                }
            }
        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
