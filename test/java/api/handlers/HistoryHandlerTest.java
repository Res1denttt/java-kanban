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
import java.util.List;

public class HistoryHandlerTest extends HandlerTest {
    @Test
    void shouldReturnHistory() throws IOException, InterruptedException {
        Task task = new Task("Task name", "Task description", Status.IN_PROGRESS, Duration.ofHours(4),
                LocalDateTime.of(2020, 5, 12, 10, 0));
        taskManager.addTask(task);
        Epic epic = new Epic("Task name", "Task description", Status.IN_PROGRESS);
        taskManager.addTask(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask description", Status.DONE, epic,
                Duration.ofHours(6), LocalDateTime.of(2025, 6, 15, 15, 30));
        taskManager.addTask(subtask);
        taskManager.getEpicById(1);
        taskManager.getTaskById(0);
        taskManager.getSubtaskById(2);
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(List.of(epic, task, subtask));
        Assertions.assertEquals(jsonString, response.body());
    }
}
