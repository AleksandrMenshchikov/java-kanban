import java.io.File;

public class Managers {

    public FileBackedTaskManager getDefauldFile(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
