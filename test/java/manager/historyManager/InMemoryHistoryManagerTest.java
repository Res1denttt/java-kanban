package manager.historyManager;

import manager.Managers;
import manager.taskManagement.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryHistoryManagerTest extends HistoryManagerTest {

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Override
    protected TaskManager createHistoryManager() {
        return Managers.getDefault();
    }
}
