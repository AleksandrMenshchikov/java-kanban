import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new Managers().getDefaultHistory();
    int taskId = 1;
    int epicId = 2;
    int subtaskId = 3;

    private void _add() {
        Task task = new Task(taskId, "t", "d", LocalDateTime.now(), Duration.ofMinutes(200));
        Epic epic = new Epic(epicId, "t", "d", null, null);
        Subtask subtask = new Subtask(subtaskId, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        Assertions.assertTrue(historyManager.getHistory().contains(task));
        Assertions.assertTrue(historyManager.getHistory().contains(epic));
        Assertions.assertTrue(historyManager.getHistory().contains(subtask));
    }

    @Test
    void add() {
        _add();
    }

    @Test
    void remove() {
        _add();
        historyManager.remove(taskId);
        historyManager.remove(epicId);
        historyManager.remove(subtaskId);
        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistory() {
        int maxReturnedElements = 1000;

        for (int i = 0; i < maxReturnedElements; i++) {
            historyManager.add(new Task(i, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100)));
        }

        Assertions.assertEquals(maxReturnedElements, historyManager.getHistory().size());
    }
}