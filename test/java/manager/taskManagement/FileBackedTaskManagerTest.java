package manager.taskManagement;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private Path path;

    @BeforeEach
    void beforeEach() throws IOException {
        path = File.createTempFile("tempFile", "csv").toPath();
        manager = FileBackedTaskManager.loadFromFile(path);
    }

    @AfterEach
    void afterEach() throws IOException {
        Files.deleteIfExists(path);
    }

    @Override
    protected FileBackedTaskManager createTaskManager() throws IOException {
        path = File.createTempFile("tempFile", "csv").toPath();
        manager = FileBackedTaskManager.loadFromFile(path);
        return manager;
    }

    @Test
    void savesAndLoadsTasksFromFile() throws IOException {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Epic epic1 = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic1);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic1);
        manager.addTask(subtask1);

        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(path);
        Assertions.assertEquals(task1, manager1.getTaskById(0));
        Assertions.assertEquals(epic1, manager1.getEpicById(1));
        Assertions.assertEquals(subtask1, manager1.getSubtaskById(2));
    }

    @Test
    void saveEmptyFile() throws IOException {
        manager.deleteAllTasks();
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Assertions.assertTrue(lines.isEmpty());
    }

    @Test
    void loadEmptyFile() {
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(path);
        Assertions.assertTrue(manager1.tasks.isEmpty() && manager1.epics.isEmpty() &&
                manager1.subtasks.isEmpty());
    }
}
