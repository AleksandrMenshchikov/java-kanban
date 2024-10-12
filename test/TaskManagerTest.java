import constants.TaskStatus;
import controllers.Managers;
import controllers.TaskManager;
import exceptions.CrossTaskException;
import models.Epic;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest {
    TaskManager taskManager = new Managers().getDefault();

    @Test
    void getTasks() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void getEpics() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
    }

    @Test
    void getSubtasks() throws CrossTaskException {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        int epicId = -1;

        for (Epic value : taskManager.getEpics()) {
            epicId = value.getId();
            break;
        }

        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createSubtask(epicId, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void getTaskById() throws CrossTaskException {
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        Integer id = null;

        for (Task value : taskManager.getTasks()) {
            id = value.getId();
            break;
        }

        Assertions.assertNotNull(taskManager.getTaskById(id));
    }

    @Test
    void getEpicById() {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Integer id = null;

        for (Task value : taskManager.getEpics()) {
            id = value.getId();
            break;
        }

        Assertions.assertNotNull(taskManager.getEpicById(id));
    }

    @Test
    void getSubtaskById() throws CrossTaskException {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Integer epicId = null;

        for (Task value : taskManager.getEpics()) {
            epicId = value.getId();
            break;
        }

        Assertions.assertNotNull(epicId);
        taskManager.createSubtask(epicId, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        int subtaskId = taskManager.getEpicById(epicId).getSubtaskIds().getFirst();
        Assertions.assertNotNull(taskManager.getSubtaskById(subtaskId));
    }

    @Test
    void getSubtasksByEpicId() throws CrossTaskException {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        int epicId = -1;

        for (Task value : taskManager.getEpics()) {
            epicId = value.getId();
            break;
        }

        taskManager.createSubtask(epicId, "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertNotNull(taskManager.getSubtasksByEpicId(epicId));
    }

    @Test
    void updateTask() throws CrossTaskException {
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(100));
        Integer taskId1 = null;
        Task task1 = null;

        for (Task value : taskManager.getTasks()) {
            taskId1 = value.getId();
            task1 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getTasks().size());
        Assertions.assertNotNull(taskId1);

        taskManager.updateTask(taskId1, "t", "d", LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(200));

        Integer taskId2 = null;
        Task task2 = null;

        for (Task value : taskManager.getTasks()) {
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
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Integer epicId1 = null;
        Epic epic1 = null;

        for (Epic value : taskManager.getEpics()) {
            epicId1 = value.getId();
            epic1 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertNotNull(epicId1);
        taskManager.updateEpic(epicId1, "t", "d", null, null);

        Integer epicId2 = null;
        Epic epic2 = null;

        for (Epic value : taskManager.getEpics()) {
            epicId2 = value.getId();
            epic2 = value;
            break;
        }

        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertNotEquals(epic1, epic2);
        Assertions.assertNotEquals(epicId1, epicId2);
    }

    @Test
    void updateSubtask() throws CrossTaskException {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
            break;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertEquals(1, epic.getSubtaskIds().size());
        Subtask subtask1 = taskManager.getSubtaskById(epic.getSubtaskIds().getFirst());
        taskManager.updateSubtask(subtask1.getId(), "t", "d", LocalDateTime.now().plusMinutes(300), Duration.ofMinutes(300));
        Assertions.assertEquals(1, epic.getSubtaskIds().size());
        Subtask subtask2 = taskManager.getSubtaskById(epic.getSubtaskIds().getFirst());
        Assertions.assertNotEquals(subtask1, subtask2);
        Assertions.assertNotEquals(subtask1.getId(), subtask2.getId());
    }

    @Test
    void createTask() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void createEpic() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
    }

    @Test
    void createSubtask() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
            break;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void deleteTask() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));

        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        Task task = null;

        for (Task value : taskManager.getTasks()) {
            task = value;
        }

        Assertions.assertNotNull(task);
        taskManager.deleteTask(task.getId());
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteEpic() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.deleteEpic(epic.getId());
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void deleteSubtask() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty());
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        Assertions.assertFalse(epic.getSubtaskIds().isEmpty());
        Subtask subtask = null;

        for (Subtask value : taskManager.getSubtasks()) {
            subtask = value;
        }

        Assertions.assertNotNull(subtask);
        taskManager.deleteSubtask(subtask.getId());
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @Test
    void updateTaskStatusOfTask() throws CrossTaskException {
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Task task = null;

        for (Task value : taskManager.getTasks()) {
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
    void updateTaskStatusOfSubtask() throws CrossTaskException {
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertSame(epic.getTaskStatus(), TaskStatus.NEW);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now().plusMinutes(300), Duration.ofMinutes(300));
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
    void clearTasks() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertFalse(taskManager.getTasks().isEmpty());
        taskManager.clearTasks();
        Assertions.assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void clearEpics() {
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Assertions.assertFalse(taskManager.getEpics().isEmpty());
        taskManager.clearEpics();
        Assertions.assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void clearSubtasks() throws CrossTaskException {
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);
        Epic epic = null;

        for (Epic value : taskManager.getEpics()) {
            epic = value;
        }

        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty());
        taskManager.clearSubtasks();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty());
    }

    @Test
    void getHistory() throws CrossTaskException {
        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now(), Duration.ofMinutes(300));
        taskManager.createEpic(taskManager.createId(), "t", "d", null, null);

        Task task = null;
        for (Task value : taskManager.getTasks()) {
            task = value;
        }

        Epic epic = null;
        for (Epic value : taskManager.getEpics()) {
            epic = value;
        }

        Assertions.assertNotNull(task);
        Assertions.assertNotNull(epic);
        taskManager.createSubtask(epic.getId(), "t", "d", LocalDateTime.now().plusMinutes(300), Duration.ofMinutes(300));

        Subtask subtask = null;
        for (Subtask value : taskManager.getSubtasks()) {
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