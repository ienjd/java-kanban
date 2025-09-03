package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
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

            String params = exchange.getRequestURI().getQuery();

            if (!exchange.getRequestURI().getQuery().substring(0,2).equals("id")) {

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
                    sendNotFound(exchange, "Добавляемая задача пересекается по времени выполнения с уже существующими", 406);
                    inMemoryTaskManager.idCount--;

                }

                inMemoryTaskManager.addTaskToList(newTask, inMemoryTaskManager.taskList);
                sendText(exchange, gson.toJson(newTask), 201);

            } else {

                String id = params.split("=")[1];

                inMemoryTaskManager.updateTask((Task) inMemoryTaskManager.taskList.get(Integer.parseInt(id)));

                sendText(exchange, "Задача обновлена", 201);
            }

        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.deleteTaskFromList(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
