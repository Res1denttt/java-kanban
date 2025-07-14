package api.handlers;

import api.HttpTaskServer;
import api.handlers.adapters.DurationAdapter;
import api.handlers.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.taskManagement.InMemoryTaskManager;
import manager.taskManagement.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class HandlerTest {
    protected TaskManager taskManager;
    protected HttpTaskServer server;
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    protected HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }

    protected HttpResponse<String> getGetResponse(URI url) throws IOException, InterruptedException {
        HttpRequest request = getGetRequest(url);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> getDeleteResponse(URI url) throws IOException, InterruptedException {
        HttpRequest request = getDeleteRequest(url);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> getPostResponse(URI url, Task task) throws IOException, InterruptedException {
        String jsonString = gson.toJson(task);
        HttpRequest request = getPostRequest(url, jsonString);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    private HttpRequest getGetRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
    }

    private HttpRequest getDeleteRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
    }

    private HttpRequest getPostRequest(URI url, String jsonString) {
        return HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
    }
}
