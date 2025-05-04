package manager.historyManagement;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> last10Tasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (last10Tasks.size() == 10) {
            last10Tasks.remove(0);
        }
        last10Tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return last10Tasks;
    }
}
