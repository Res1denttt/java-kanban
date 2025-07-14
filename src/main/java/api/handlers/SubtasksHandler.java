package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManagement.ManagerSaveException;
import manager.taskManagement.NotFoundException;
import manager.taskManagement.TaskManager;
import model.Subtask;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        switch (exchange.getRequestMethod()) {
            case "GET" -> handleGet(splitPath, exchange);
            case "DELETE" -> handleDelete(splitPath, exchange);
            case "POST" -> handlePost(exchange);
        }
    }

    private void handleGet(String[] splitPath, HttpExchange exchange) {
        try {
            if (splitPath.length > 2) {
                getById(splitPath, exchange);
            } else {
                getSubtasks(exchange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void getById(String[] splitPath, HttpExchange exchange) throws IOException, NotFoundException {
        long id = Long.parseLong(splitPath[2]);
        Subtask subtask = taskManager.getSubtaskById(id);
        String response = gson.toJson(subtask);
        sendText(exchange, response);
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllSubtasks());
        sendText(exchange, response);
    }

    private void handleDelete(String[] splitPath, HttpExchange exchange) {
        long id = Long.parseLong(splitPath[2]);
        try {
            taskManager.deleteSubtaskById(id);
            exchange.sendResponseHeaders(200, -1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendInternalServerError(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) {
        String request;
        try {
            request = new String(exchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Subtask subtask = gson.fromJson(request, Subtask.class);
        int result;
        try {
            taskManager.getSubtaskById(subtask.getId());
            result = taskManager.updateTask(subtask);
        } catch (NotFoundException e) {
            result = taskManager.addTask(subtask);
        } catch (ManagerSaveException e) {
            sendInternalServerError(exchange);
            return;
        }
        if (result > 0) {
            try {
                exchange.sendResponseHeaders(201, -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendHasInteractions(exchange);
        }
    }
}
