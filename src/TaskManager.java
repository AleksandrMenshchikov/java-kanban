import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Task getTaskById(Integer taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(Integer epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(Integer subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Subtask> getSubtaskArrayListByEpicId(Integer epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Subtask> subtaskArrayList = new ArrayList<>();

            for (Integer subtaskId : epics.get(epicId).getSubtaskIds().values()) {
                subtaskArrayList.add(subtasks.get(subtaskId));
            }

            return subtaskArrayList;
        }

        return null;
    }

    public void updateTaskById(Integer taskId, Task task) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpicById(Integer epicId, Epic epic) {
        if (epics.containsKey(epicId)) {
            Epic epic1 = epics.get(epicId);

            for (Integer value : epic1.getSubtaskIds().values()) {
                if (subtasks.containsKey(value)) {
                    subtasks.get(value).setEpicId(null);
                }
            }

            epics.remove(epicId);
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubtaskById(Integer subtaskId, Subtask subtask) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
            epic.deleteSubtaskId(subtaskId);
            subtasks.remove(subtaskId);
            subtasks.put(subtask.getId(), subtask);
        }
    }

    public void putTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void putEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void putSubtask(int epicId, Subtask subtask) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            epic.addSubtask(subtask.getId());
            subtask.setEpicId(epicId);
            subtasks.put(subtask.getId(), subtask);
        }
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);

            for (Integer subtaskId : epic.getSubtaskIds().values()) {
                subtasks.get(subtaskId).setEpicId(null);
            }

            epics.remove(epicId);
        }
    }

    public void removeSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
            epic.deleteSubtaskId(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    public void setTaskStatusForTask(int taskId, TaskStatus taskStatus) {
        if (tasks.containsKey(taskId)) {
            tasks.get(taskId).setTaskStatus(taskStatus);
        }
    }

    public void setTaskStatusForSubtask(int subtaskId, TaskStatus taskStatus) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            subtask.setTaskStatus(taskStatus);
            Epic epic = epics.get(subtask.getEpicId());

            int listSize = epic.getSubtaskIds().size();
            HashMap<TaskStatus, Integer> hashMap = new HashMap<>();

            for (int id : epic.getSubtaskIds().values()) {
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

    public void clearTaskHashMap() {
        tasks.clear();
    }

    public void clearEpicHashMap() {
        epics.clear();
    }

    public void clearSubtaskHashMap() {
        subtasks.clear();
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
