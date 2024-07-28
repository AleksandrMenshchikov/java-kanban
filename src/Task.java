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

    protected final int getId() {
        return id;
    }

    protected final void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    protected final TaskStatus getTaskStatus() {
        return taskStatus;
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
}
