package api.handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManagement.TaskManager;


public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(exchange, response);
    }
}
