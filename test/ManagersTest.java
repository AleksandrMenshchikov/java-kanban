import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void getDefault() {
        Assertions.assertInstanceOf(TaskManager.class, Managers.getDefault());
    }

    @Test
    void getDefaultHistory() {
        Assertions.assertInstanceOf(HistoryManager.class, Managers.getDefaultHistory());
    }
}