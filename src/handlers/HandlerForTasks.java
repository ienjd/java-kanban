package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import tasks.Task;

public class HandlerForTasks<T extends InMemoryTaskManager> extends BaseHttpHandler {

    private final T taskManager;

    public HandlerForTasks(T taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("tasks")) {
                sendText(exchange, gson.toJson(taskManager.getTaskList().values()), 200);
                return;
            }

            if (taskManager.getTaskList().containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(taskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Task newTask = gson.fromJson(body, Task.class);

            if (taskManager.getTaskList().containsKey(newTask.getId())) {
                taskManager.updateTask(taskManager.findTask(newTask.getId()), newTask.getStatus());
                sendText(exchange, gson.toJson(newTask), 201);
            } else if (!taskManager.getTaskList().containsKey(newTask.getId())) {
                try {
                    taskManager.addTaskToList(newTask, taskManager.getTaskList());
                    sendText(exchange, gson.toJson(newTask), 201);
                } catch (ManagerSaveException e) {
                    sendText(exchange, gson.toJson(e.getMessage()), 406);
                }
            }
        } else if (reqMethod.equals("DELETE")) {

            taskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
