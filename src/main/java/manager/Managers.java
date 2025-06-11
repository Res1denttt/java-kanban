package manager;

import manager.historyManagement.HistoryManager;
import manager.historyManagement.InMemoryHistoryManager;
import manager.taskManagement.FileBackedTaskManager;
import manager.taskManagement.ManagerLoadException;
import manager.taskManagement.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    public static TaskManager getDefault() {
        Path path = Paths.get("tasks.csv");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new ManagerLoadException();
            }

        }
        return FileBackedTaskManager.loadFromFile(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
