import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    private final int id = 1;
    private final String title = "t";
    private final String description = "d";
    private final int num1 = 1;
    private final int num2 = 2;
    private final Epic epic = new Epic(id, title, description);

    @Test
    void addSubtask() {
        epic.addSubtask(num1);
        epic.addSubtask(num2);
        Assertions.assertTrue(epic.getSubtaskIds().contains(num1));
        Assertions.assertTrue(epic.getSubtaskIds().contains(num2));
    }

    @Test
    void getSubtaskIds() {
        addSubtask();
    }

    @Test
    void deleteSubtaskId() {
        addSubtask();
        epic.deleteSubtaskId(num1);
        Assertions.assertFalse(epic.getSubtaskIds().contains(num1));
        epic.deleteSubtaskId(num2);
        Assertions.assertFalse(epic.getSubtaskIds().contains(num2));
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(epic, new Epic(id, title, description));
    }

    @Test
    void testHashCode() {
        Assertions.assertEquals(epic.hashCode(), new Epic(id, title, description).hashCode());
    }
}