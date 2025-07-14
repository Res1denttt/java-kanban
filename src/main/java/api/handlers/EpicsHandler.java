package api.handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManagement.ManagerSaveException;
import manager.taskManagement.NotFoundException;
import manager.taskManagement.TaskManager;
import model.Epic;

import java.io.IOException;


public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    public EpicsHandler(TaskManager taskManager) {
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
            if (splitPath.length > 3) {
                getSubtaskList(splitPath, exchange);
            } else if (splitPath.length > 2) {
                getById(splitPath, exchange);
            } else {
                getEpics(exchange);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        }
    }

    private void getById(String[] splitPath, HttpExchange exchange) throws IOException, NotFoundException {
        long id = Long.parseLong(splitPath[2]);
        Epic epic = taskManager.getEpicById(id);
        String response = gson.toJson(epic);
        sendText(exchange, response);

    }

    private void getEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getAllEpics());
        sendText(exchange, response);
    }

    private void handleDelete(String[] splitPath, HttpExchange exchange) {
        long id = Long.parseLong(splitPath[2]);
        try {
            taskManager.deleteEpicById(id);
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
        Epic epic = gson.fromJson(request, Epic.class);
        int result;
        try {
            taskManager.getEpicById(epic.getId());
            result = taskManager.updateTask(epic);
        } catch (NotFoundException e) {
            result = taskManager.addTask(epic);
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

    private void getSubtaskList(String[] splitPath, HttpExchange exchange) {
        long epicId;
        try {
            epicId = Long.parseLong(splitPath[2]);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
            return;
        }
        String response = gson.toJson(taskManager.getEpicSubtasks(taskManager.getEpicById(epicId)));
        sendText(exchange, response);
    }
}
