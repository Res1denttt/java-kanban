package api.handlers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class PrioritizedHandlerTest extends HandlerTest {

    @Test
    void shouldReturnPrioritizedList() throws IOException, InterruptedException {
        Task task = new Task("Task name", "Task description", Status.IN_PROGRESS, Duration.ofHours(4),
                LocalDateTime.of(2020, 5, 12, 10, 0));
        taskManager.addTask(task);
        Epic epic = new Epic("Epic name", "Epic description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        taskManager.addTask(subtask);
        Task task2 = new Task("Another Task name", "Another Task description", Status.NEW, Duration.ofHours(40),
                LocalDateTime.of(2022, 5, 12, 10, 0));
        taskManager.addTask(task2);
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getPrioritizedTasks());
        Assertions.assertEquals(jsonString, response.body());
    }
}
