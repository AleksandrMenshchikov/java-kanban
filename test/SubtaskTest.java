import models.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {
    int epicId = 123;
    Subtask subtask = new Subtask(1, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));

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