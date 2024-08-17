import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new Managers().getDefaultHistory();

    @Test
    void add() {
        Task task = new Task(1, "t", "d");
        historyManager.add(task);
        Assertions.assertTrue(historyManager.getHistory().contains(task));
    }

    @Test
    void getHistory() {
        int maxReturnedElements = 10;

        for (int i = 0; i < maxReturnedElements + 1; i++) {
            historyManager.add(new Task(i, "t", "d"));
        }

        Assertions.assertEquals(maxReturnedElements, historyManager.getHistory().size());
    }
}