package api.handlers;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtasksHandlerTest extends HandlerTest {
    @Test
    void shouldReturnAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        taskManager.addTask(new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30)));
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(taskManager.getAllSubtasks().isEmpty());
        String jsonString = gson.toJson(taskManager.getAllSubtasks());
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturnSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        taskManager.addTask(new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30)));
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getSubtaskById(1));
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturn404IfTaskIsNotFound() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        taskManager.addTask(new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30)));
        URI url = URI.create("http://localhost:8080/subtasks/24");
        HttpResponse<String> response = getGetResponse(url);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        taskManager.addTask(new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30)));
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpResponse<String> response = getDeleteResponse(url);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllSubtasks().isEmpty());
        Assertions.assertTrue(response.body().isEmpty());
    }

    @Test
    void shouldCreateNewSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        URI url = URI.create("http://localhost:8080/subtasks");
        Assertions.assertTrue(taskManager.getAllSubtasks().isEmpty());
        HttpResponse<String> response = getPostResponse(url, subtask);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertFalse(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        taskManager.addTask(subtask);
        Subtask subtask2 = new Subtask("Another Subtask name", "Another Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        subtask2.setId(subtask.getId());
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpResponse<String> response = getPostResponse(url, subtask2);
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Another Subtask name", taskManager.getSubtaskById(1).getName());
    }

    @Test
    void ShouldReturn406IfCrossOtherTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofDays(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        taskManager.addTask(subtask);
        Subtask subtask2 = new Subtask("Another Subtask name", "Another Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 18, 10, 30));
        subtask2.setId(25);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpResponse<String> response = getPostResponse(url, subtask2);
        Assertions.assertEquals(406, response.statusCode());
        Assertions.assertEquals(1, taskManager.getAllSubtasks().size());
    }
}
