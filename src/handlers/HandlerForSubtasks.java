package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import tasks.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HandlerForSubtasks<T extends InMemoryTaskManager> extends BaseHttpHandler implements HttpHandler {

    private final T taskManager;

    public HandlerForSubtasks(T taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] splitReq = exchange.getRequestURI().getPath().split("/");

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {

            if (Arrays.stream(splitReq).toList().getLast().equals("subtasks")) {
                sendText(exchange, gson.toJson(taskManager.subtaskList.values()), 200);
                return;
            }

            if (taskManager.subtaskList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(taskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 201);
            } else {
                sendNotFound(exchange, "Задачи с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Subtask newTask = gson.fromJson(body, Subtask.class);
            System.out.println(newTask);

            if (taskManager.subtaskList.containsKey(newTask.getId())) {
                taskManager.updateSubtask(taskManager.findTask(newTask.getId()));
                sendText(exchange, gson.toJson(newTask), 201);
            } else if (!taskManager.subtaskList.containsKey(newTask.getId())) {
                if (taskManager.isOverLapping(newTask)) {
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                } else {
                    taskManager.addTaskToList(newTask, taskManager.subtaskList);
                    taskManager.setEpicStartTime(newTask.getEpicId());
                    taskManager.setEpicDuration(newTask.getEpicId());
                    sendText(exchange, gson.toJson(newTask), 201);
                }
            }

        } else if (reqMethod.equals("DELETE")) {

            taskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
