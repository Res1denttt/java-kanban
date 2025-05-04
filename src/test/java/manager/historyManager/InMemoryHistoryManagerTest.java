package manager.historyManager;

import manager.historyManagement.HistoryManager;
import manager.taskManagement.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    HistoryManager historyManager = manager.getHistoryManager();

    @Test
    void savesPreviousVersionOfTask() {
        Epic epic = new Epic("Abc", "Some description", Status.NEW);
        manager.addTask(epic);
        manager.getEpicById(epic.getId());
        Epic historyEpic = (Epic) historyManager.getHistory().getFirst();
        Assertions.assertEquals(epic, historyEpic);
        long id = historyEpic.getId();
        Subtask newSubtask = new Subtask("Lpo", "Another description", Status.IN_PROGRESS, epic);
        newSubtask.setId(id);
        manager.updateTask(newSubtask);
        Assertions.assertEquals(epic, historyEpic);
    }
}
