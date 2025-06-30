package manager.taskManagement;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Override
    protected InMemoryTaskManager createTaskManager() throws IOException {
        return new InMemoryTaskManager();
    }
}
