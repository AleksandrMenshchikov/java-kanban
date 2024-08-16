import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();


    @Override
    public void add(Task task) {
        int maxSize = 10;

        if (history.size() - maxSize >= 0) {
            history.add(task);
            history.removeFirst();
        } else {
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
