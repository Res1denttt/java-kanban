package api.handlers;

import api.handlers.adapters.DurationAdapter;
import api.handlers.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManagement.ManagerSaveException;
import manager.taskManagement.NotFoundException;
import manager.taskManagement.TaskManager;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
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
                getTasks(exchange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void getById(String[] splitPath, HttpExchange exchange) throws IOException, NotFoundException {
        long id = Long.parseLong(splitPath[2]);
        Task task = taskManager.getTaskById(id);
        String response = gson.toJson(task);
        sendText(exchange, response);
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllTasks());
        sendText(exchange, response);
    }

    private void handleDelete(String[] splitPath, HttpExchange exchange) {
        long id = Long.parseLong(splitPath[2]);
        try {
            taskManager.deleteTaskById(id);
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
        Task task = gson.fromJson(request, Task.class);
        int result;
        try {
            taskManager.getTaskById(task.getId());
            result = taskManager.updateTask(task);
        } catch (NotFoundException e) {
            result = taskManager.addTask(task);
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
