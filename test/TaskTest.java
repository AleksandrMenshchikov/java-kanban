import constants.TaskStatus;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    int id = 1;
    String title = "t";
    String description = "d";
    LocalDateTime localDateTime = LocalDateTime.now();
    Task task = new Task(id, title, description, localDateTime, Duration.ofMinutes(100));

    @Test
    void getId() {
        Assertions.assertEquals(task.getId(), id);
    }

    @Test
    void getTitle() {
        Assertions.assertEquals(title, task.getTitle());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals(description, task.getDescription());
    }

    @Test
    void setTaskStatus() {
        Assertions.assertNull(task.getTaskStatus());
        task.setTaskStatus(TaskStatus.NEW);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.NEW);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.IN_PROGRESS);
        task.setTaskStatus(TaskStatus.DONE);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void getTaskStatus() {
        Assertions.assertNull(task.getTaskStatus());
        task.setTaskStatus(TaskStatus.NEW);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.NEW);
    }

    @Test
    void testEquals() {
        Task task1 = new Task(id, title, description, localDateTime, Duration.ofMinutes(100));
        Assertions.assertEquals(task, task1);
    }

    @Test
    void testHashCode() {
        Task task1 = new Task(id, title, description, localDateTime, Duration.ofMinutes(100));
        Assertions.assertEquals(task.hashCode(), task1.hashCode());
    }
}