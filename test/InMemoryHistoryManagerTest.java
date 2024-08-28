import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new Managers().getDefaultHistory();
    int taskId = 1;
    int epicId = 2;
    int subtaskId = 3;

    @Test
    void add() {
        Task task = new Task(taskId, "t", "d");
        Epic epic = new Epic(epicId, "t", "d");
        Subtask subtask = new Subtask(subtaskId, "t", "d");
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        Assertions.assertTrue(historyManager.getHistory().contains(task));
        Assertions.assertTrue(historyManager.getHistory().contains(epic));
        Assertions.assertTrue(historyManager.getHistory().contains(subtask));
    }

    @Test
    void remove() {
        add();
        historyManager.remove(taskId);
        historyManager.remove(epicId);
        historyManager.remove(subtaskId);
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistory() {
        int maxReturnedElements = 1000;

        for (int i = 0; i < maxReturnedElements; i++) {
            historyManager.add(new Task(i, "t", "d"));
        }

        Assertions.assertEquals(maxReturnedElements, historyManager.getHistory().size());
    }
}