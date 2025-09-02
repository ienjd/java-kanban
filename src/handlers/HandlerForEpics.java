package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import tasks.Status;

import java.io.IOException;

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
            }

            if (Arrays.stream(splitReq).toList().getLast().equals("subtasks")) {
                String[] params = exchange.getRequestURI().getPath().split("/");

                if (inMemoryTaskManager.epicList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().get(splitReq.length - 2)))) {
                    Epic epic = (Epic) inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                            .toList().get(splitReq.length - 2)));

                    sendText(exchange, gson.toJson(epic.getSubtasks()), 200);
                } else {
                    sendNotFound(exchange, "Эпика с таким id нет в списке!", 404);
                }
            }

            if (inMemoryTaskManager.epicList.containsKey(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()))) {
                sendText(exchange, gson.toJson(inMemoryTaskManager.findTask(Integer.parseInt(Arrays.stream(splitReq)
                        .toList().getLast()))), 200);
            } else {
                sendNotFound(exchange, "Эпика с таким id нет в списке!", 404);
            }

        } else if (reqMethod.equals("POST")) {

            String params = exchange.getRequestURI().getQuery();

            String title = ((params.split("&"))[0]).split("=")[1];
            String descr = ((params.split("&"))[1]).split("=")[1];

            Epic newEpic;
            if (inMemoryTaskManager.getEpicSubtasks(inMemoryTaskManager.getIdCount()).isEmpty()) {
                newEpic = new Epic(title, descr, ++inMemoryTaskManager.idCount, Status.NEW);
            } else {
                newEpic = inMemoryTaskManager.createEpic(title, descr);
                inMemoryTaskManager.setEpicDuration(newEpic.getId());
                inMemoryTaskManager.setEpicStartTime(newEpic.getId());
            }

            inMemoryTaskManager.addTaskToList(newEpic, inMemoryTaskManager.epicList);
            sendText(exchange, gson.toJson(newEpic), 201);

        } else if (reqMethod.equals("DELETE")) {

            inMemoryTaskManager.epicList.remove(Integer.parseInt(Arrays.stream(splitReq).toList().getLast()));

            sendText(exchange, "Задача удалена", 200);
        }
    }
}
