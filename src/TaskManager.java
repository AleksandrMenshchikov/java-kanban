import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, Subtask> getSubtasks();

    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer epicId);

    Subtask getSubtaskById(Integer subtaskId);

    ArrayList<Subtask> getSubtasksByEpicId(int epicId);

    void updateTask(int taskId, String title, String description);

    void updateEpic(int epicId, String title, String description);

    void updateSubtask(int subtaskId, String title, String description);

    Task createTask(String title, String description);

    Epic createEpic(String title, String description);

    Subtask createSubtask(int epicId, String title, String description);

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
