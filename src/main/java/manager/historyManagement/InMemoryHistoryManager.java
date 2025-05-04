package manager.historyManagement;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> lastViewedTasks = new ArrayList<>();
    private static final int MAX_LIST_SIZE = 10;

    @Override
    public void add(Task task) {
        if (lastViewedTasks.size() == MAX_LIST_SIZE) {
            lastViewedTasks.remove(0);
        }
        lastViewedTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return lastViewedTasks;
    }
}
