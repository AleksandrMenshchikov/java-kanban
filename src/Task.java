import java.util.HashMap;

public class Task {
    private final String title;
    private final String description;
    private TaskStatus taskStatus;
    private final int id;
    private final HashMap<Integer, Task> taskHashMap;

    protected Task(int id, String title, String description) {
        taskHashMap = new HashMap<>();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    protected final int getId() {
        return id;
    }

    protected final void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public final TaskStatus getTaskStatus() {
        return taskStatus;
    }

    protected final void setTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    protected final HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    @Override
    public final String toString() {
        return getClass().getName() + "{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", id=" + id +
                ", " + getClass().getName() + "HashMap=" + taskHashMap +
                '}';
    }
}
