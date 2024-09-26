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
    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    static int createId() {
        return ++counter;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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

    private boolean checkCrossTask(TreeSet<Task> prioritizedTasks, Task task) {
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
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
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
    public void updateTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration) {
        Task oldTask = tasks.get(taskId);

        if (oldTask != null) {
            Task task = new Task(createId(), title, description, startTime, duration);

            if (checkCrossTask(getPrioritizedTasks(), task)) {
                throw new RuntimeException("Задача пересекается по времени выполнения");
            }

            tasks.remove(taskId);
            removePrioritizedTask(oldTask);
            tasks.put(task.getId(), task);
            task.setTaskStatus(TaskStatus.NEW);
            addPrioritizedTask(task);
        }
    }

    @Override
    public void updateEpicTime(Epic epic) {
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();

        List<Subtask> list = getSubtasks().values().stream().filter(elem -> subtaskIds.contains(elem.getId())).sorted(Comparator.comparing(Task::getStartTime)).toList();
        epic.setStartTime(list.getFirst().getStartTime());

        List<Subtask> list1 = list.stream().sorted((a, b) -> {
            if (Objects.requireNonNull(a.getEndTime()).isAfter(b.getEndTime())) {
                return -1;
            } else if (a.getEndTime().equals(b.getEndTime())) {
                return 0;
            } else {
                return 1;
            }
        }).toList();
        Duration between = Duration.between(list.getFirst().getStartTime(), list1.getFirst().getEndTime());
        epic.setDuration(between);
    }

    @Override
    public void updateEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
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
        }
    }

    @Override
    public void updateSubtask(int subtaskId, String title, String description, LocalDateTime startTime, Duration duration) {
        Subtask s = getSubtaskById(subtaskId);

        if (s != null) {
            Subtask subtask = new Subtask(createId(), title, description, startTime, duration);

            if (checkCrossTask(getPrioritizedTasks(), subtask)) {
                throw new RuntimeException("Задача пересекается по времени выполнения");
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
        }
    }

    @Override
    public Task createTask(String title, String description, LocalDateTime startTime, Duration duration) {
        Task task = new Task(createId(), title, description, startTime, duration);

        if (checkCrossTask(getPrioritizedTasks(), task)) {
            throw new RuntimeException("Задача пересекается по времени выполнения");
        }

        tasks.put(task.getId(), task);
        task.setTaskStatus(TaskStatus.NEW);
        addPrioritizedTask(task);
        return task;
    }

    @Override
    public Epic createEpic(String title, String description, LocalDateTime startTime, Duration duration) {
        Epic epic = new Epic(createId(), title, description, startTime, duration);
        epics.put(epic.getId(), epic);
        epic.setTaskStatus(TaskStatus.NEW);
        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
        if (epics.containsKey(epicId)) {
            Subtask subtask = new Subtask(createId(), title, description, startTime, duration);

            if (checkCrossTask(getPrioritizedTasks(), subtask)) {
                throw new RuntimeException("Задача пересекается по времени выполнения");
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
            if (prioritizedTask.getClass().getName().equals("Task")) {
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
            if (prioritizedTask.getClass().getName().equals("Subtask")) {
                removePrioritizedTask(prioritizedTask);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
