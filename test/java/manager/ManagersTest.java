package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void returnsNotNullTaskManager() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    void returnsNotNullHistoryManager() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}
