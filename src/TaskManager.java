import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer epicId);

    Subtask getSubtaskById(Integer subtaskId);

    List<Subtask> getSubtasksByEpicId(int epicId);

    void updateTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration);

    void updateEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration);

    void updateEpicTime(Epic epic);

    void updateSubtask(int subtaskId, String title, String description, LocalDateTime startTime, Duration duration);

    Task createTask(String title, String description, LocalDateTime startTime, Duration duration);

    Epic createEpic(String title, String description, LocalDateTime startTime, Duration duration);

    Subtask createSubtask(int epicId, String title, String description, LocalDateTime startTime, Duration duration);

    void deleteTask(int taskId);

    void deleteEpic(int epicId);

    void deleteSubtask(int subtaskId);

    void updateTaskStatusOfTask(int taskId, TaskStatus taskStatus);

    void updateTaskStatusOfSubtask(int subtaskId, TaskStatus taskStatus);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    List<Task> getHistory();
}
