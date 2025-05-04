package manager;

import manager.historyManagement.HistoryManager;
import manager.historyManagement.InMemoryHistoryManager;
import manager.taskManagement.InMemoryTaskManager;
import manager.taskManagement.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
