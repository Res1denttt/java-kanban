package manager.historyManagement;

import model.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();

    public void remove(long id);
}
