import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {
    int id = 1;
    String title = "t";
    String description = "d";
    Task task = new Task(id, title, description);

    @Test
    void getId() {
        Assertions.assertEquals(task.getId(), id);
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
        Task task1 = new Task(id, title, description);
        Assertions.assertEquals(task, task1);
    }

    @Test
    void testHashCode() {
        Task task1 = new Task(id, title, description);
        Assertions.assertEquals(task.hashCode(), task1.hashCode());
    }
}