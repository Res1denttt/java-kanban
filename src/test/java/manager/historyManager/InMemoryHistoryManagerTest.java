package manager.historyManager;

import manager.taskManagement.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryHistoryManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

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
}
