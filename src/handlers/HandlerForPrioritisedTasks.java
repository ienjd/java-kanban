package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import java.io.IOException;

public class HandlerForPrioritisedTasks<T extends InMemoryTaskManager> extends BaseHttpHandler {

    private final T taskManager;

    public HandlerForPrioritisedTasks(T taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {
            sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        }
    }
}
