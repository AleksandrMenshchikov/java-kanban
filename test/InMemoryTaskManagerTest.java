import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {
    TaskManager taskManager = new Managers().getDefault();

    @Test
    void getTasks() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask("t", "d");
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void getEpics() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic("t", "d");
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
    }

    @Test
    void getSubtasks() {
        taskManager.createEpic("t", "d");
        int epicId = -1;

        for (Epic value : taskManager.getEpics().values()) {
            epicId = value.getId();
            break;
        }

        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createSubtask(epicId, "t", "d");
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void getTaskById() {
        taskManager.createTask("t", "d");
        Integer id = null;

        for (Task value : taskManager.getTasks().values()) {
            id = value.getId();
            break;
        }

        Assertions.assertNotNull(taskManager.getTaskById(id));
    }

    @Test
    void getEpicById() {
        taskManager.createEpic("t", "d");
        Integer id = null;

        for (Task value : taskManager.getEpics().values()) {
            id = value.getId();
            break;
        }

        Assertions.assertNotNull(taskManager.getEpicById(id));
    }

    @Test
    void getSubtaskById() {
        taskManager.createEpic("t", "d");
        Integer epicId = null;

        for (Task value : taskManager.getEpics().values()) {
            epicId = value.getId();
            break;
        }

        Assertions.assertNotNull(epicId);
        taskManager.createSubtask(epicId, "t", "d");
        int subtaskId = taskManager.getEpicById(epicId).getSubtaskIds().getFirst();
        Assertions.assertNotNull(taskManager.getSubtaskById(subtaskId));
    }

    @Test
    void getSubtasksByEpicId() {
        taskManager.createEpic("t", "d");
        int epicId = -1;

        for (Task value : taskManager.getEpics().values()) {
            epicId = value.getId();
            break;
        }

        taskManager.createSubtask(epicId, "t", "d");
        Assertions.assertNotNull(taskManager.getSubtasksByEpicId(epicId));
    }

    @Test
    void updateTask() {
        taskManager.createTask("t", "d");
        Integer taskId1 = null;
        Task task1 = null;

        for (Task value : taskManager.getTasks().values()) {
            taskId1 = value.getId();
            task1 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getTasks().size());
        Assertions.assertNotNull(taskId1);
        taskManager.updateTask(taskId1, "t", "d");

        Integer taskId2 = null;
        Task task2 = null;

        for (Task value : taskManager.getTasks().values()) {
            taskId2 = value.getId();
            task2 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getTasks().size());
        Assertions.assertNotEquals(task1, task2);
        Assertions.assertNotEquals(taskId1, taskId2);
    }

    @Test
    void updateEpic() {
        taskManager.createEpic("t", "d");
        Integer epicId1 = null;
        Epic epic1 = null;

        for (Epic value : taskManager.getEpics().values()) {
            epicId1 = value.getId();
            epic1 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertNotNull(epicId1);
        taskManager.updateEpic(epicId1, "t", "d");

        Integer epicId2 = null;
        Epic epic2 = null;

        for (Epic value : taskManager.getEpics().values()) {
            epicId2 = value.getId();
            epic2 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertNotEquals(epic1, epic2);
        Assertions.assertNotEquals(epicId1, epicId2);
    }

    @Test
    void updateSubtask() {
        taskManager.createEpic("t", "d");
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
            break;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertEquals(1, epic.getSubtaskIds().size());
        Subtask subtask1 = taskManager.getSubtaskById(epic.getSubtaskIds().getFirst());
        taskManager.updateSubtask(subtask1.getId(), "t", "d");
        Assertions.assertEquals(1, epic.getSubtaskIds().size());
        Subtask subtask2 = taskManager.getSubtaskById(epic.getSubtaskIds().getFirst());
        Assertions.assertNotEquals(subtask1, subtask2);
        Assertions.assertNotEquals(subtask1.getId(), subtask2.getId());
    }

    @Test
    void createTask() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask("t", "d");
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void createEpic() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic("t", "d");
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
    }

    @Test
    void createSubtask() {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic("t", "d");
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
            break;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteTask() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask("t", "d");
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        Task task = null;

        for (Task value : taskManager.getTasks().values()) {
            task = value;
        }

        Assertions.assertNotNull(task);
        taskManager.deleteTask(task.getId());
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteEpic() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic("t", "d");
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.deleteEpic(epic.getId());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void deleteSubtask() {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic("t", "d");
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty());
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        Assertions.assertFalse(epic.getSubtaskIds().isEmpty());
        Subtask subtask = null;

        for (Subtask value : taskManager.getSubtasks().values()) {
            subtask = value;
        }

        Assertions.assertNotNull(subtask);
        taskManager.deleteSubtask(subtask.getId());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @Test
    void updateTaskStatusOfTask() {
        taskManager.createTask("t", "d");
        Task task = null;

        for (Task value : taskManager.getTasks().values()) {
            task = value;
        }

        Assertions.assertNotNull(task);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.NEW);
        taskManager.updateTaskStatusOfTask(task.getId(), TaskStatus.IN_PROGRESS);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatusOfTask(task.getId(), TaskStatus.DONE);
        Assertions.assertSame(task.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void updateTaskStatusOfSubtask() {
        taskManager.createEpic("t", "d");
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.NEW);
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.NEW);
        Subtask subtask1 = taskManager.getSubtaskById(epic.getSubtaskIds().getFirst());
        Subtask subtask2 = taskManager.getSubtaskById(epic.getSubtaskIds().get(1));
        taskManager.updateTaskStatusOfSubtask(subtask1.getId(), TaskStatus.IN_PROGRESS);
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatusOfSubtask(subtask2.getId(), TaskStatus.DONE);
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatusOfSubtask(subtask1.getId(), TaskStatus.DONE);
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.DONE);
    }

    @Test
    void clearTasks() {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask("t", "d");
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        taskManager.clearTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void clearEpics() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic("t", "d");
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        taskManager.clearEpics();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void clearSubtasks() {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic("t", "d");
        Epic epic = null;

        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d");
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        taskManager.clearSubtasks();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void getHistory() {
        taskManager.createTask("t", "d");
        taskManager.createEpic("t", "d");

        Task task = null;
        for (Task value : taskManager.getTasks().values()) {
            task = value;
        }

        Epic epic = null;
        for (Epic value : taskManager.getEpics().values()) {
            epic = value;
        }

        Assertions.assertNotNull(task);
        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d");

        Subtask subtask = null;
        for (Subtask value : taskManager.getSubtasks().values()) {
            subtask = value;
        }

        Assertions.assertNotNull(subtask);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        Assertions.assertTrue(taskManager.getHistory().contains(task));
        Assertions.assertTrue(taskManager.getHistory().contains(epic));
        Assertions.assertTrue(taskManager.getHistory().contains(subtask));
    }
}