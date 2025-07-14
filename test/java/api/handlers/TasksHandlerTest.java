package api.handlers;


import model.Status;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandlerTest extends HandlerTest {
    @Test
    void shouldReturnAllTasks() throws IOException, InterruptedException {
        taskManager.addTask(new Task("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(taskManager.getAllTasks().isEmpty());
        String jsonString = gson.toJson(taskManager.getAllTasks());
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturnTaskById() throws IOException, InterruptedException {
        taskManager.addTask(new Task("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getTaskById(0));
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturn404IfTaskIsNotFound() throws IOException, InterruptedException {
        taskManager.addTask(new Task("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/tasks/24");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        taskManager.addTask(new Task("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpResponse<String> response = getDeleteResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllTasks().isEmpty());
        Assertions.assertTrue(response.body().isEmpty());
    }

    @Test
    void shouldCreateNewTask() throws IOException, InterruptedException {
        Task task = new Task("Task name", "Task description", Status.IN_PROGRESS, Duration.ofHours(4), LocalDateTime.of(2020, 5, 12, 10, 0));
        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = getPostResponse(url, task);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(task, taskManager.getTaskById(0));
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Task name", "Task description", Status.IN_PROGRESS, Duration.ofHours(4), LocalDateTime.of(2020, 5, 12, 10, 0));
        taskManager.addTask(task);
        Task task2 = new Task("Task name", "Task description", Status.DONE, Duration.ofHours(4), LocalDateTime.of(2020, 5, 12, 10, 0));
        task2.setId(task.getId());
        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = getPostResponse(url, task2);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(Status.DONE, taskManager.getTaskById(0).getStatus());
    }

    @Test
    void ShouldReturn406IfCrossOtherTask() throws IOException, InterruptedException {
        Task task = new Task("Task name", "Task description", Status.IN_PROGRESS, Duration.ofHours(4), LocalDateTime.of(2020, 5, 12, 10, 0));
        taskManager.addTask(task);
        Task task2 = new Task("Task name", "Task description", Status.DONE, Duration.ofHours(4), LocalDateTime.of(2020, 5, 12, 10, 0));
        task2.setId(25);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = getPostResponse(url, task2);
        Assertions.assertEquals(406, response.statusCode());
        Assertions.assertEquals(1, taskManager.getAllTasks().size());
    }
}
