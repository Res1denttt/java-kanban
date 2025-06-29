package manager.taskManagement;


import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    protected abstract T createTaskManager() throws IOException;

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
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        Assertions.assertEquals(2, manager.getAllTasks().size());
        Assertions.assertEquals(2, manager.getHistory().size());
        manager.deleteTaskById(0);
        Assertions.assertEquals(1, manager.getAllTasks().size());
        Assertions.assertEquals(task2, manager.getAllTasks().getFirst());
        Assertions.assertEquals(1, manager.getHistory().size());
    }

    @Test
    void shouldDeleteEpicById() {
        Epic epic1 = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic1);
        Epic epic2 = new Epic("Qwe", "Some description", Status.DONE);
        manager.addTask(epic2);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic1);
        manager.addTask(subtask1);
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask1.getId());
        Assertions.assertEquals(2, manager.getAllEpics().size());
        Assertions.assertEquals(1, manager.getAllSubtasks().size());
        Assertions.assertEquals(3, manager.getHistory().size());
        manager.deleteEpicById(0);
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(epic2, manager.getAllEpics().getFirst());
        Assertions.assertEquals(0, manager.getAllSubtasks().size());
        Assertions.assertEquals(1, manager.getHistory().size());
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
    void shouldReturnPrioritizedTasks() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic, Duration.ofHours(25),
                LocalDateTime.of(2024, 4, 24, 10, 40));
        manager.addTask(subtask2);
        Subtask subtask3 = new Subtask("Lop", "Some description", Status.DONE, epic);
        manager.addTask(subtask3);
        Set<Task> tasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().get()));
        tasks.add(subtask1);
        tasks.add(epic);
        Assertions.assertEquals(manager.getPrioritizedTasks(), tasks);
    }

    @Test
    void epicTimeCalculatesWithSubtasks() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Assertions.assertEquals(subtask2.getStartTime().get(), epic.getStartTime().get());
        Assertions.assertEquals(subtask1.getEndTime().get(), epic.getEndTime().get());
    }

    @Test
    void crossedTasksShouldNotBaAdded() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Task task = new Task("Uty", "One more task", Status.IN_PROGRESS, Duration.ofDays(300),
                LocalDateTime.of(2024, 3, 12, 12, 10));
        manager.addTask(task);
        Assertions.assertEquals(2, manager.getAllSubtasks().size());
        Assertions.assertEquals(1, manager.getAllEpics().size());
        Assertions.assertEquals(0, manager.getAllTasks().size());
    }

    @Test
    void epicShouldBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic name", "Epic description", Status.DONE);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.NEW, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void epicShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.DONE, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.DONE, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void epicShouldBeInProgressWhenSubtasksNewAndDone() {
        Epic epic = new Epic("Epic name", "Epic description", Status.DONE);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.NEW, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.DONE, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void epicShouldBeInProgressWhenSubtasksInProgress() {
        Epic epic = new Epic("Epic name", "Epic description", Status.DONE);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.IN_PROGRESS, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.IN_PROGRESS, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void shouldDeleteAllSubtasks() {
        Task task = new Task("Uty", "One more task", Status.IN_PROGRESS, Duration.ofDays(300),
                LocalDateTime.of(2024, 3, 12, 12, 10));
        manager.addTask(task);
        Epic epic = new Epic("Epic name", "Epic description", Status.NEW);
        manager.addTask(epic);
        Subtask subtask1 = new Subtask("Abc", "Some description", Status.DONE, epic, Duration.ofHours(10),
                LocalDateTime.of(2024, 6, 24, 8, 30));
        manager.addTask(subtask1);
        Subtask subtask2 = new Subtask("Qwe", "Some description", Status.DONE, epic, Duration.ofHours(25),
                LocalDateTime.of(2022, 5, 14, 18, 40));
        manager.addTask(subtask2);
        manager.deleteAllSubtasks();
        Assertions.assertTrue(manager.getAllSubtasks().isEmpty());
        Assertions.assertEquals(1, manager.getPrioritizedTasks().size());
        Assertions.assertEquals(0, epic.getSubtaskList().size());
    }
}
