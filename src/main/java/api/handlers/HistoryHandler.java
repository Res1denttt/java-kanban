package api.handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManagement.TaskManager;


public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String response = gson.toJson(taskManager.getHistory());
        sendText(exchange, response);
    }
}
