import java.util.HashMap;

public final class TaskManager {
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();

    public void setTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void setTaskStatusForTask(int taskId, TaskStatus taskStatus) {
        if (taskHashMap.containsKey(taskId)) {
            Task task = taskHashMap.get(taskId);
            task.setTaskStatus(taskStatus);
        }
    }

    public void deleteTaskById(int taskId) {
        taskHashMap.remove(taskId);
    }

    public void setEpic(int taskId, Epic epic) {
        if (taskHashMap.containsKey(taskId)) {
            Task task = taskHashMap.get(taskId);
            task.setTask(epic);
        }
    }

    public void deleteEpicById(int taskId, int epicId) {
        if (taskHashMap.containsKey(taskId)) {
            Task task = taskHashMap.get(taskId);
            task.getTaskHashMap().remove(epicId);
        }
    }

    public void setSubtask(int taskId, int epicId, Subtask subtask) {
        if (taskHashMap.containsKey(taskId)) {
            Task task = taskHashMap.get(taskId);

            if (task.getTaskHashMap().containsKey(epicId)) {
                Epic epic = (Epic) task.getTaskHashMap().get(epicId);
                epic.setTask(subtask);
            }
        }
    }

    public void setTaskStatusForSubtask(int taskId, int epicId, int subtaskId, TaskStatus taskStatus) {
        if (taskHashMap.containsKey(taskId)) {
            Task task = taskHashMap.get(taskId);

            if (task.getTaskHashMap().containsKey(epicId)) {
                Epic epic = (Epic) task.getTaskHashMap().get(epicId);

                if (epic.getTaskHashMap().containsKey(subtaskId)) {
                    Subtask subtask = (Subtask) epic.getTaskHashMap().get(subtaskId);
                    subtask.setTaskStatus(taskStatus);
                }

                int length = epic.getTaskHashMap().values().size();
                HashMap<TaskStatus, Integer> hashMap = new HashMap<>();
                for (Task subtask : epic.getTaskHashMap().values()) {
                    switch (subtask.getTaskStatus()) {
                        case NEW -> {
                            hashMap.put(TaskStatus.NEW, hashMap.get(TaskStatus.NEW) == null ? 1 : hashMap.get(TaskStatus.NEW) + 1);
                        }
                        case DONE -> {
                            hashMap.put(TaskStatus.DONE, hashMap.get(TaskStatus.NEW) == null ? 1 : hashMap.get(TaskStatus.NEW) + 1);
                        }
                        case IN_PROGRESS -> {
                            hashMap.put(TaskStatus.IN_PROGRESS, hashMap.get(TaskStatus.NEW) == null ? 1 : hashMap.get(TaskStatus.NEW) + 1);
                        }
                        case null, default -> {
                        }
                    }
                }

                if (hashMap.get(TaskStatus.NEW) != null && hashMap.get(TaskStatus.NEW) == length) {
                    epic.setTaskStatus(TaskStatus.NEW);
                } else if (hashMap.get(TaskStatus.DONE) != null && hashMap.get(TaskStatus.DONE) == length) {
                    epic.setTaskStatus(TaskStatus.DONE);
                } else {
                    epic.setTaskStatus(TaskStatus.IN_PROGRESS);
                }
            }
        }
    }

    public void printData() {
        System.out.println(taskHashMap);
    }
}
