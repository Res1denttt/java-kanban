package manager.taskManagement;

import manager.Managers;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManagerTest {
    FileBackedTaskManager manager;
    Path path;

    @BeforeEach
    void beforeEach() throws IOException {
        path = File.createTempFile("tempFile", "csv").toPath();
        manager = new FileBackedTaskManager(path);
    }

    @Test
    void shouldAddTask() {
        manager.addTask(new Task("Abc", "Some description", Status.NEW));
        List<Task> allTasks = manager.getAllTasks();
        Assertions.assertFalse(allTasks.isEmpty());
    }

    @Test
    void shouldAddEpic() {
        manager.addTask(new Epic("Qwe", "Some description", Status.NEW));
        List<Epic> allEpics = manager.getAllEpics();
        Assertions.assertFalse(allEpics.isEmpty());
    }

    @Test
    void shouldAddSubtask() {
        Epic epic = new Epic("Qwe", "Some description", Status.NEW);
        manager.addTask(new Subtask("Zxc", "Some description", Status.NEW, epic));
        List<Subtask> allSubtasks = manager.getAllSubtasks();
        Assertions.assertFalse(allSubtasks.isEmpty());
    }

    @Test
    void shouldFindTaskById() {
        Task task = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task);
        long id = task.getId();
        Assertions.assertEquals(task, manager.getTaskById(id));
    }

    @Test
    void shouldFindEpicById() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic);
        long id = epic.getId();
        Assertions.assertEquals(epic, manager.getEpicById(id));
    }

    @Test
    void shouldFindSubtaskById() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        Subtask subtask = new Subtask("Abc", "Some description", Status.NEW, epic);
        manager.addTask(subtask);
        long id = subtask.getId();
        Assertions.assertEquals(subtask, manager.getSubtaskById(id));
    }

    @Test
    void setIdAndGeneratedIdShouldNotConflict() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic);
        Assertions.assertEquals(0, epic.getId());
        Subtask subtask = new Subtask("Qwe", "Some description", Status.NEW, epic);
        subtask.setId(0);
        manager.addTask(subtask);
        Assertions.assertEquals(1, subtask.getId());
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(1, manager.getAllSubtasks().size());
    }

    @Test
    void taskShouldNotAlterAfterBeenAdded() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        Assertions.assertTrue(epic.getSubtaskList().isEmpty());
        manager.addTask(epic);
        Assertions.assertEquals("Abc", epic.getName());
        Assertions.assertEquals("Some description", epic.getDescription());
        Assertions.assertTrue(epic.getSubtaskList().isEmpty());
        Subtask subtask = new Subtask("Qwe", "Another description", Status.DONE, epic);
        manager.addTask(subtask);
        Assertions.assertEquals("Qwe", subtask.getName());
        Assertions.assertEquals("Another description", subtask.getDescription());
        Assertions.assertEquals(Status.DONE, subtask.getStatus());
        Assertions.assertEquals(epic, subtask.getEpic());
    }

    @Test
    void shouldDeleteTaskById() {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Qwe", "Some description", Status.NEW);
        manager.addTask(task2);
        Assertions.assertEquals(2, manager.getAllTasks().size());
        manager.deleteTaskById(0);
        Assertions.assertEquals(1, manager.getAllTasks().size());
        Assertions.assertEquals(task2, manager.getAllTasks().getFirst());
    }

    @Test
    void shouldDeleteEpicById() {
        Epic epic1 = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic1);
        Epic epic2 = new Epic("Qwe", "Some description", Status.DONE);
        manager.addTask(epic2);
        Assertions.assertEquals(2, manager.getAllEpics().size());
        manager.deleteEpicById(0);
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(epic2, manager.getAllEpics().getFirst());
    }

    @Test
    void shouldDeleteSubtaskById() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic);
        manager.addTask(subtask2);
        Assertions.assertEquals(2, manager.getAllSubtasks().size());
        manager.deleteSubtaskById(1);
        Assertions.assertEquals(1, manager.getAllSubtasks().size());
        Assertions.assertEquals(subtask2, manager.getAllSubtasks().getFirst());
    }

    @Test
    void updateEpicTest() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        long id = epic.getId();
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic);
        manager.addTask(subtask2);
        Epic newEpic = new Epic("Another Epic name", "Another Epic description", Status.DONE);
        newEpic.setId(id);
        manager.updateTask(newEpic);
        Assertions.assertEquals(List.of(subtask1, subtask2), manager.getEpicSubtasks(newEpic));
        Assertions.assertEquals(newEpic, manager.getEpicById(id));
    }

    @Test
    void epicShouldNotContainDeletedSubtask() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic);
        manager.addTask(subtask2);
        manager.deleteSubtaskById(subtask1.getId());
        Assertions.assertEquals(List.of(subtask2), epic.getSubtaskList());
    }

    @Test
    void savesTasksInFile() throws IOException {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Epic epic1 = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic1);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic1);
        manager.addTask(subtask1);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Assertions.assertEquals(task1, manager.fromString(lines.getFirst()));
        Assertions.assertEquals(epic1, manager.fromString(lines.get(1)));
        Assertions.assertEquals(subtask1, manager.fromString(lines.get(2)));
    }

    @Test
    void loadsTasksFromFile() throws IOException {
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
        manager.save();
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
