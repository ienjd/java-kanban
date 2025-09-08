package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import tasks.Task;
import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForTasks extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();


        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("tasks")) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.taskList.values()), 200);
                return;
            }

            if (inMemoryTaskManager.taskList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Task newTask = gson.fromJson(body, Task.class);

            if (inMemoryTaskManager.taskList.containsKey(newTask.getId())) {
                inMemoryTaskManager.updateTask(inMemoryTaskManager.findTask(newTask.getId()));
                sendText(exchange, gson.toJson(newTask), 201);
            } else if (!inMemoryTaskManager.taskList.containsKey(newTask.getId())) {
                if (inMemoryTaskManager.isOverLapping(newTask)) {
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                } else {
                    inMemoryTaskManager.addTaskToList(newTask, inMemoryTaskManager.taskList);
                    sendText(exchange, gson.toJson(newTask), 201);
                }
            }
        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
