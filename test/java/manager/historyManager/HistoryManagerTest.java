package manager.historyManager;

import manager.taskManagement.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

abstract class HistoryManagerTest {
    protected TaskManager manager;

    protected abstract TaskManager createHistoryManager();

    @Test
    void shouldReturnEmptyHistory() {
        Assertions.assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void shouldNotDuplicateTasks() {
        Epic epic2 = new Epic("Lpo", "Another description", Status.IN_PROGRESS);
        manager.addTask(epic2);
        Subtask subtask3 = new Subtask("Hia", "One more description", Status.DONE, epic2);
        manager.addTask(subtask3);
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask3.getId());
        manager.getEpicById(epic2.getId());
        Assertions.assertEquals(2, manager.getHistory().size());
    }

    @Test
    void shouldRemoveFirstTaskFromHistory() {
        Task task1 = new Task("Abc", "Some description", Status.NEW);
        manager.addTask(task1);
        Epic epic2 = new Epic("Lpo", "Another description", Status.IN_PROGRESS);
        manager.addTask(epic2);
        Epic epic3 = new Epic("Hia", "One more description", Status.DONE);
        manager.addTask(epic3);
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic3.getId());
        Assertions.assertEquals(List.of(epic2, task1, epic3), manager.getHistory());
        manager.deleteEpicById(epic2.getId());
        Assertions.assertEquals(List.of(task1, epic3), manager.getHistory());
    }

    @Test
    void shouldRemoveLastTaskFromHistory() {
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
        manager.deleteSubtaskById(subtask3.getId());
        Assertions.assertEquals(List.of(epic2, task1), manager.getHistory());
    }

    @Test
    void shouldRemoveMiddleTaskFromHistory() {
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
        manager.deleteTaskById(task1.getId());
        Assertions.assertEquals(List.of(epic2, subtask3), manager.getHistory());
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

    @Test
    void shouldDeleteAllTasks() {
        Task task = new Task("Uty", "One more task", Status.IN_PROGRESS);
        manager.addTask(task);
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.DONE, epic);
        manager.addTask(subtask1);
        Task task2 = new Task("Myu", "to do", Status.NEW);
        manager.addTask(task2);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getTaskById(task2.getId());
        Assertions.assertEquals(4, manager.getHistory().size());
        manager.deleteAllTasks();
        Assertions.assertEquals(2, manager.getHistory().size());
    }

    @Test
    void shouldDeleteAllEpicsAndSubtasks() {
        Epic epic2 = new Epic("Uty", "One more task", Status.IN_PROGRESS);
        manager.addTask(epic2);
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.DONE, epic);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.DONE, epic2);
        manager.addTask(subtask2);
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getEpicById(epic2.getId());
        Assertions.assertEquals(4, manager.getHistory().size());
        manager.deleteAllEpics();
        Assertions.assertEquals(0, manager.getHistory().size());
    }

    @Test
    void shouldDeleteAllSubtasks() {
        Task task = new Task("Uty", "One more task", Status.IN_PROGRESS);
        manager.addTask(task);
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.DONE, epic);
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.DONE, epic);
        manager.addTask(subtask2);
        manager.getTaskById(task.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        Assertions.assertEquals(4, manager.getHistory().size());
        manager.deleteAllSubtasks();
        Assertions.assertEquals(2, manager.getHistory().size());
    }
}
