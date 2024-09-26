import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final Integer id;
    private final String title;
    private final String description;
    private TaskStatus taskStatus;
    private LocalDateTime startTime;
    private Duration duration;

    protected Task(int id, String title, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public final int getId() {
        return id;
    }

    protected final TaskStatus getTaskStatus() {
        return taskStatus;
    }

    protected final void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    protected final String getTitle() {
        return title;
    }

    protected final String getDescription() {
        return description;
    }

    protected final LocalDateTime getStartTime() {
        return startTime;
    }

    protected final void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected final Duration getDuration() {
        return duration;
    }

    protected final void setDuration(Duration duration) {
        this.duration = duration;
    }

    protected final LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }

        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && taskStatus == task.taskStatus && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, taskStatus, startTime, duration);
    }
}
