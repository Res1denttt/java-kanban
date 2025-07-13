package api;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.taskManagement.TaskManager;


import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    HttpServer httpServer;
    TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        this.taskManager = taskManager;
    }

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        taskManager = Managers.getDefault();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }

    public void start() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
