import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    private static int counter;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int createId() {
        return ++counter;
    }

    private HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    private HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    private HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    private Task getTaskById(Integer taskId) {
        return tasks.get(taskId);
    }

    private Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }

    private Subtask getSubtaskById(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Subtask> subtasks = new ArrayList<>();

            for (Integer subtaskId : getEpicById(epicId).getSubtaskIds()) {
                subtasks.add(getSubtaskById(subtaskId));
            }

            return subtasks.isEmpty() ? null : subtasks;
        }

        return null;
    }

    public void updateTask(int taskId, String title, String description) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            Task task = new Task(createId(), title, description);
            tasks.put(task.getId(), task);
            task.setTaskStatus(TaskStatus.NEW);
        }
    }

    public void updateEpic(int epicId, String title, String description) {
        if (epics.containsKey(epicId)) {
            for (Integer subtaskId : getEpicById(epicId).getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }

            epics.remove(epicId);
            Epic epic1 = new Epic(createId(), title, description);
            epics.put(epic1.getId(), epic1);
            epic1.setTaskStatus(TaskStatus.NEW);
        }
    }

    public void updateSubtask(int subtaskId, String title, String description) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = getEpicById(getSubtaskById(subtaskId).getEpicId());
            Subtask subtask = new Subtask(createId(), title, description);
            subtask.setEpicId(epic.getId());
            epic.addSubtask(subtask.getId());
            epic.deleteSubtaskId(subtaskId);
            subtasks.remove(subtaskId);
            subtasks.put(subtask.getId(), subtask);
            updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.NEW);
        }
    }

    public void createTask(String title, String description) {
        Task task = new Task(createId(), title, description);
        tasks.put(task.getId(), task);
        task.setTaskStatus(TaskStatus.NEW);
    }

    public void createEpic(String title, String description) {
        Epic epic = new Epic(createId(), title, description);
        epics.put(epic.getId(), epic);
        epic.setTaskStatus(TaskStatus.NEW);
    }

    public void createSubtask(int epicId, String title, String description) {
        if (epics.containsKey(epicId)) {
            Epic epic = getEpicById(epicId);
            Subtask subtask = new Subtask(createId(), title, description);
            epic.addSubtask(subtask.getId());
            subtask.setEpicId(epicId);
            subtasks.put(subtask.getId(), subtask);
            updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.NEW);
        }
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = getEpicById(epicId);

            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }

            epics.remove(epicId);
        }
    }

    public void deleteSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = getEpicById(getSubtaskById(subtaskId).getEpicId());
            epic.deleteSubtaskId(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    public void updateTaskStatusOfTask(int taskId, TaskStatus taskStatus) {
        if (tasks.containsKey(taskId)) {
            getTaskById(taskId).setTaskStatus(taskStatus);
        }
    }

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

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        clearSubtasks();
    }

    public void clearSubtasks() {
        subtasks.clear();

        for (Integer id : epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
        }
    }

    public void printAllData() {
        if (!tasks.isEmpty()) {
            System.out.println(getTasks());
        }

        if (!epics.isEmpty()) {
            System.out.println(getEpics());
        }

        if (!subtasks.isEmpty()) {
            System.out.println(getSubtasks());
        }
    }
}
