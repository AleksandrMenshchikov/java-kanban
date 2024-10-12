package models;

import constants.TaskStatus;
import controllers.HistoryManager;
import controllers.TaskManager;
import exceptions.CrossTaskException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    private static int counter;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public int createId() {
        return ++counter;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addPrioritizedTask(Task task) {
        if (task.getStartTime() == null) return;
        prioritizedTasks.add(task);
    }

    private void removePrioritizedTask(Task task) {
        if (task != null) {
            prioritizedTasks.remove(task);
        }
    }

    private boolean checkCrossTask(Task task) {
        for (Task prioritizedTask : prioritizedTasks) {
            if (task.getStartTime().equals(prioritizedTask.getStartTime()) ||
                    task.getStartTime().equals(prioritizedTask.getEndTime()) ||
                    Objects.equals(task.getEndTime(), prioritizedTask.getStartTime()) ||
                    Objects.equals(task.getEndTime(), prioritizedTask.getEndTime()) ||
                    (task.getStartTime().isAfter(prioritizedTask.getStartTime()) &&
                            task.getStartTime().isBefore(prioritizedTask.getEndTime())) ||
                    (Objects.requireNonNull(task.getEndTime()).isAfter(prioritizedTask.getStartTime()) &&
                            task.getEndTime().isBefore(prioritizedTask.getEndTime()))) {
                return true;

            }
        }

        return false;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        if (epics.containsKey(epicId)) {
            List<Subtask> subtasks = getEpicById(epicId).getSubtaskIds().stream().map(this::getSubtaskById).toList();

            return subtasks.isEmpty() ? null : subtasks;
        }

        return null;
    }

    @Override
    public Task updateTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Task oldTask = tasks.get(taskId);

        if (oldTask != null) {
            Task task = new Task(createId(), title, description, startTime, duration);

            if (checkCrossTask(task)) {
                throw new CrossTaskException();
            }

            tasks.remove(taskId);
            removePrioritizedTask(oldTask);
            tasks.put(task.getId(), task);
            task.setTaskStatus(TaskStatus.NEW);
            addPrioritizedTask(task);
            return task;
        }

        return null;
    }

    @Override
    public void updateEpicTime(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();

        List<Subtask> list = getSubtasks().stream().filter(elem -> subtaskIds.contains(elem.getId())).sorted(Comparator.comparing(Task::getStartTime)).toList();
        epic.setStartTime(list.getFirst().getStartTime());
        epic.setEndTime(list.getLast().getEndTime());

        Duration duration = list.stream()
                .map((Task::getDuration))
                .reduce((Duration::plus))
                .orElse(null);

        epic.setDuration(duration);
    }

    @Override
    public Epic updateEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
        if (epics.containsKey(epicId)) {
            for (Integer subtaskId : getEpicById(epicId).getSubtaskIds()) {
                subtasks.remove(subtaskId);
                Subtask subtask = getSubtaskById(subtaskId);

                if (subtask != null) {
                    removePrioritizedTask(subtask);
                }
            }

            epics.remove(epicId);
            Epic epic1 = new Epic(createId(), title, description, startTime, duration);
            epics.put(epic1.getId(), epic1);
            epic1.setTaskStatus(TaskStatus.NEW);
            return epic1;
        }
        return null;
    }

    @Override
    public Subtask updateSubtask(int subtaskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Subtask s = getSubtaskById(subtaskId);

        if (s != null) {
            Subtask subtask = new Subtask(createId(), title, description, startTime, duration);

            if (checkCrossTask(subtask)) {
                throw new CrossTaskException();
            }

            Epic epic = getEpicById(s.getEpicId());
            subtask.setEpicId(epic.getId());
            epic.deleteSubtaskId(subtaskId);
            epic.addSubtask(subtask.getId());
            removePrioritizedTask(s);
            subtasks.remove(subtaskId);
            subtasks.put(subtask.getId(), subtask);
            updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.NEW);
            updateEpicTime(epic);
            addPrioritizedTask(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public Task createTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Task task = new Task(taskId, title, description, startTime, duration);

        if (checkCrossTask(task)) {
            throw new CrossTaskException();
        }

        tasks.put(task.getId(), task);
        task.setTaskStatus(TaskStatus.NEW);
        addPrioritizedTask(task);
        return task;
    }

    @Override
    public Epic createEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
        Epic epic = new Epic(epicId, title, description, startTime, duration);
        epics.put(epic.getId(), epic);
        epic.setTaskStatus(TaskStatus.NEW);
        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        if (epics.containsKey(epicId)) {
            Subtask subtask = new Subtask(createId(), title, description, startTime, duration);

            if (checkCrossTask(subtask)) {
                throw new CrossTaskException();
            }

            Epic epic = getEpicById(epicId);
            epic.addSubtask(subtask.getId());
            subtask.setEpicId(epicId);
            subtasks.put(subtask.getId(), subtask);
            updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.NEW);
            updateEpicTime(epic);
            addPrioritizedTask(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        removePrioritizedTask(getTaskById(taskId));
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = getEpicById(epicId);

            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                removePrioritizedTask(getSubtaskById(subtaskId));
                historyManager.remove(subtaskId);
            }

            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Subtask s = getSubtaskById(subtaskId);
        if (s != null) {
            Epic epic = getEpicById(s.getEpicId());
            epic.deleteSubtaskId(subtaskId);
            subtasks.remove(subtaskId);
            removePrioritizedTask(s);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void updateTaskStatusOfTask(int taskId, TaskStatus taskStatus) {
        if (tasks.containsKey(taskId)) {
            getTaskById(taskId).setTaskStatus(taskStatus);
        }
    }

    @Override
    public void updateTaskStatusOfSubtask(int subtaskId, TaskStatus taskStatus) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = getSubtaskById(subtaskId);
            subtask.setTaskStatus(taskStatus);
            Epic epic = getEpicById(subtask.getEpicId());

            int listSize = epic.getSubtaskIds().size();
            HashMap<TaskStatus, Integer> hashMap = new HashMap<>();

            for (Integer id : epic.getSubtaskIds()) {
                switch (subtasks.get(id).getTaskStatus()) {
                    case NEW -> hashMap.compute(TaskStatus.NEW, (k, integer) -> integer == null ? 1 : integer + 1);
                    case DONE -> hashMap.compute(TaskStatus.DONE, (k, integer) -> integer == null ? 1 : integer + 1);
                    case null, default -> {
                    }
                }
            }

            if (hashMap.containsKey(TaskStatus.NEW) && hashMap.get(TaskStatus.NEW) == listSize) {
                epic.setTaskStatus(TaskStatus.NEW);
            } else if (hashMap.containsKey(TaskStatus.DONE) && hashMap.get(TaskStatus.DONE) == listSize) {
                epic.setTaskStatus(TaskStatus.DONE);
            } else {
                epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();

        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getClass().getSimpleName().equals("model.Task")) {
                removePrioritizedTask(prioritizedTask);
            }
        }
    }

    @Override
    public void clearEpics() {
        epics.clear();
        clearSubtasks();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();

        for (Integer id : epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
        }

        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getClass().getSimpleName().equals("model.Subtask")) {
                removePrioritizedTask(prioritizedTask);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
