package controllers;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskManager {
    int createId();

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    List<Subtask> getSubtasksByEpicId(int epicId);

    void updateTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration);

    void updateEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration);

    void updateEpicTime(Epic epic);

    void updateSubtask(int subtaskId, String title, String description, LocalDateTime startTime, Duration duration);

    Task createTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration);

    Epic createEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration);

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

    List<Task> getPrioritizedTasks();
}
