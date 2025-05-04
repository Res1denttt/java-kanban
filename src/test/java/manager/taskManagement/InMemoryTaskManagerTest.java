package manager.taskManagement;

import manager.Managers;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class InMemoryTaskManagerTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
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
        Assertions.assertEquals(1, epic.getId());
        Subtask subtask = new Subtask("Qwe", "Some description", Status.NEW, epic);
        subtask.setId(1);
        manager.addTask(subtask);
        Assertions.assertEquals(2, subtask.getId());
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(1, manager.getAllSubtasks().size());
    }

    @Test
    void taskShouldNotAlterAfterBeenAdded() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        Assertions.assertTrue(epic.getSubtaskSet().isEmpty());
        manager.addTask(epic);
        Assertions.assertEquals("Abc", epic.getName());
        Assertions.assertEquals("Some description", epic.getDescription());
        Assertions.assertTrue(epic.getSubtaskSet().isEmpty());
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
        manager.deleteTaskById(1);
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
        manager.deleteEpicById(1);
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
        manager.deleteSubtaskById(2);
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
        Assertions.assertEquals(Set.of(subtask1, subtask2), manager.getEpicSubtasks(newEpic));
        Assertions.assertEquals(newEpic, manager.getEpicById(id));
    }
}
