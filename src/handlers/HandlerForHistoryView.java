package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static main.HttpTaskServer.inMemoryTaskManager;

public class HandlerForHistoryView extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(inMemoryTaskManager.getHistory()));
    }
}
