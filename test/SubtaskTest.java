import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    int epicId = 123;
    Subtask subtask = new Subtask(1, "t", "d");

    @Test
    void getEpicId() {
        Assertions.assertNull(subtask.getEpicId());
        subtask.setEpicId(epicId);
        Assertions.assertEquals(subtask.getEpicId(), epicId);
    }

    @Test
    void setEpicId() {
        Assertions.assertNull(subtask.getEpicId());
        subtask.setEpicId(epicId);
        Assertions.assertEquals(subtask.getEpicId(), epicId);
    }
}