package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import java.io.IOException;

public class HandlerForHistoryView<T extends InMemoryTaskManager> extends BaseHttpHandler implements HttpHandler {

    private final T taskManager;

    public HandlerForHistoryView(T taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod();

        if (reqMethod.equals("GET")) {
            sendText(exchange, gson.toJson(taskManager.getHistory()), 200);
        }
    }
}
