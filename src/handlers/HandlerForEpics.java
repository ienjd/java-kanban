package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerSaveException;
import manager.InMemoryTaskManager;
import tasks.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HandlerForEpics<T extends InMemoryTaskManager> extends BaseHttpHandler {

    private final T taskManager;

    public HandlerForEpics(T taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("epics")) {
                sendText(exchange, gson.toJson(taskManager.getEpicList().values()), 200);

            } else if (Arrays.stream(splitReq).toList().getLast().equals("subtasks")) {
                sendText(exchange, gson.toJson(taskManager.getEpicSubtasks(Integer.parseInt(splitReq[splitReq.length - 2]))), 200);
            }

            if (taskManager.getEpicList().containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(taskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Epic newTask = gson.fromJson(body, Epic.class);

            try {
                taskManager.addTaskToList(newTask, taskManager.getEpicList());
                sendText(exchange, gson.toJson(newTask), 201);
            } catch (ManagerSaveException e) {
                sendText(exchange, gson.toJson(e.getMessage()), 406);
            }

        } else if (reqMethod.equals("DELETE")) {

            taskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
