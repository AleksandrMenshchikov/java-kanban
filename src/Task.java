import java.util.Objects;

public class Task {
    private final Integer id;
    private final String title;
    private final String description;
    private TaskStatus taskStatus;

    protected Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public final int getId() {
        return id;
    }

    protected final void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    protected final TaskStatus getTaskStatus() {
        return taskStatus;
    }

    protected final String getTitle() {
        return title;
    }

    protected final String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, taskStatus);
    }
}
