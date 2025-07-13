package api.handlers;

import api.HttpTaskServer;
import api.handlers.adapters.DurationAdapter;
import api.handlers.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.taskManagement.InMemoryTaskManager;
import manager.taskManagement.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;
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
}
