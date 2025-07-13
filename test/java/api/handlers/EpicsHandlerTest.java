package api.handlers;


import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicsHandlerTest extends HandlerTest {
    @Test
    void shouldReturnAllEpics() throws IOException, InterruptedException {
        taskManager.addTask(new Epic("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertFalse(taskManager.getAllEpics().isEmpty());
        String jsonString = gson.toJson(taskManager.getAllEpics());
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturnEpicById() throws IOException, InterruptedException {
        taskManager.addTask(new Epic("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getEpicById(0));
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturn404IfEpicIsNotFound() throws IOException, InterruptedException {
        taskManager.addTask(new Epic("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/epics/24");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        taskManager.addTask(new Epic("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(taskManager.getAllEpics().isEmpty());
        Assertions.assertTrue(response.body().isEmpty());
    }

    @Test
    void shouldCreateNewEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        String jsonString = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals(epic, taskManager.getEpicById(0));
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Epic epic2 = new Epic("Epic name", "Another description", Status.DONE);
        epic2.setId(epic.getId());
        String jsonString = gson.toJson(epic2);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("Another description", taskManager.getEpicById(0).getDescription());
    }

    @Test
    void shouldReturnEpicsSubtaskList() throws IOException, InterruptedException {
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        taskManager.addTask(subtask);
        URI url = URI.create("http://localhost:8080/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(List.of(subtask));
        Assertions.assertEquals(jsonString, response.body());
    }

    @Test
    void shouldReturn404IfEpicIsNotFoundForSubtasks() throws IOException, InterruptedException {
        taskManager.addTask(new Epic("Task name", "Task description", Status.IN_PROGRESS));
        URI url = URI.create("http://localhost:8080/epics/24/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }
}
