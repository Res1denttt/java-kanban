package manager.historyManager;

import manager.Managers;
import manager.taskManagement.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryHistoryManagerTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void savesPreviousVersionOfTask() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic);
        manager.getEpicById(epic.getId());
        List<Task> lastViewedTasks = manager.getHistory();
        Epic historyEpic = (Epic) lastViewedTasks.getFirst();
        Assertions.assertEquals(epic, historyEpic);
        long id = historyEpic.getId();
        Subtask newSubtask = new Subtask("Lpo", "Another description", Status.IN_PROGRESS, epic);
        newSubtask.setId(id);
        manager.updateTask(newSubtask);
        Assertions.assertEquals(epic, historyEpic);
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Epic epic2 = new Epic("Lpo", "Another description", Status.IN_PROGRESS);
        manager.addTask(epic2);
        Subtask subtask3 = new Subtask("Hia", "One more description", Status.DONE, epic2);
        manager.addTask(subtask3);
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task1.getId());
        Assertions.assertEquals(List.of(epic2, task1), manager.getHistory());
        manager.deleteEpicById(epic2.getId());
        Assertions.assertEquals(List.of(task1), manager.getHistory());
    }

    @Test
    void historyShouldBeEmptyAfterRemovingLastTask() {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Lpo", "Another description", Status.IN_PROGRESS);
        manager.addTask(task2);
        manager.getTaskById(task2.getId());
        Assertions.assertEquals(List.of(task2), manager.getHistory());
        manager.deleteTaskById(task2.getId());
        Assertions.assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void shouldReturnCorrectOrderOfViewedTasks() {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Epic epic2 = new Epic("Lpo", "Another description", Status.IN_PROGRESS);
        manager.addTask(epic2);
        Subtask subtask3 = new Subtask("Hia", "One more description", Status.DONE, epic2);
        manager.addTask(subtask3);
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task1.getId());
        manager.getSubtaskById(subtask3.getId());
        Assertions.assertEquals(List.of(epic2, task1, subtask3), manager.getHistory());
    }

//    @Test
//    void
}
