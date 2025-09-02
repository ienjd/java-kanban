package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
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

            String params = exchange.getRequestURI().getQuery();

            if (!exchange.getRequestURI().getQuery().substring(0,2).equals("id")) {

                System.out.println(exchange.getRequestURI().getQuery().substring(0,2));
                String title = ((params.split("&"))[0]).split("=")[1];
                String descr = ((params.split("&"))[1]).split("=")[1];
                String duration = ((params.split("&"))[2]).split("=")[1];
                String startTime = ((params.split("&"))[3]).split("=")[1];
                String epicId = ((params.split("&"))[4]).split("=")[1];

                Subtask newSubtask = inMemoryTaskManager.createSubtask(title, descr, Integer.parseInt(epicId));
                newSubtask.setDuration(Integer.parseInt(duration));
                newSubtask.setStartTime(LocalDateTime.of(
                        Integer.parseInt(startTime.substring(6, 10)),
                        Integer.parseInt(startTime.substring(3, 5)),
                        Integer.parseInt(startTime.substring(0, 2)),
                        Integer.parseInt(startTime.substring(11, 13)),
                        Integer.parseInt(startTime.substring(14, 16))));

                inMemoryTaskManager.sortingTasks();

                if (inMemoryTaskManager.isOverLapping(newSubtask)) {
                    sendNotFound(exchange,
                            "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                    inMemoryTaskManager.idCount--;
                }

                inMemoryTaskManager.addTaskToList(newSubtask, inMemoryTaskManager.subtaskList);

                System.out.println( inMemoryTaskManager.subtaskList);
                sendText(exchange, gson.toJson(newSubtask), 201);

            } else {

                String id = params.split("=")[1];

                inMemoryTaskManager.updateSubtask((Subtask) inMemoryTaskManager.subtaskList.get(Integer.parseInt(id)));

                sendText(exchange, "Задача обновлена", 201);
            }

        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
