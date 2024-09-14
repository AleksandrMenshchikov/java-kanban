import Exceptions.ManagerSaveException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        loadFromFile(file);
    }

    private boolean isFileExists() {
        Path path = null;

        try {
            path = file.toPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path != null) {
            return Files.exists(path);
        }

        return false;
    }

    private void loadFromFile(File file) {
        boolean fileExists = isFileExists();
        if (!fileExists) return;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String str = bufferedReader.readLine();
                if (str.isBlank()) continue;
                String[] split = str.split(",");
                if (split[0].equals("id")) continue;

                Integer id = null;
                try {
                    id = Integer.parseInt(split[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (id == null) return;

                String taskType, title, taskStatus, description;
                try {
                    taskType = split[1];
                    title = split[2];
                    taskStatus = split[3];
                    description = split[4];
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return;
                }


                if (taskType.equals(TaskType.TASK.toString())) {
                    Task task = new Task(id, title, description);

                    try {
                        task.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }

                    HashMap<Integer, Task> tasks = getTasks();
                    tasks.put(id, task);
                    createId();
                } else if (taskType.equals(TaskType.EPIC.toString())) {
                    Epic epic = new Epic(id, title, description);

                    try {
                        epic.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }

                    String s;
                    try {
                        s = split[5].split("=")[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    String[] split1;
                    try {
                        split1 = s.substring(1, s.length() - 1).split(";");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    for (String string : split1) {
                        Integer num = null;
                        try {
                            num = Integer.parseInt(string.trim());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (num == null) return;

                        epic.addSubtask(num);
                    }

                    HashMap<Integer, Epic> epics = getEpics();
                    epics.put(id, epic);
                    createId();
                } else if (taskType.equals(TaskType.SUBTASK.toString())) {
                    Subtask subtask = new Subtask(id, title, description);

                    try {
                        subtask.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    String s;
                    try {
                        s = split[5].split("=")[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    try {
                        subtask.setEpicId(Integer.valueOf(s));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }

                    HashMap<Integer, Subtask> subtasks = getSubtasks();
                    subtasks.put(id, subtask);
                    createId();
                }
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException(e.getMessage());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String toString(Task task) {
        if (task == null) {
            return null;
        }

        String taskClassName = task.getClass().getName().toUpperCase();
        String baseString = task.getId() + "," + taskClassName + "," + task.getTitle() + "," + task.getTaskStatus() + "," + task.getDescription();
        String result = null;

        if (taskClassName.equals(TaskType.TASK.toString())) {
            result = baseString;
        } else if (taskClassName.equals(TaskType.EPIC.toString())) {
            Epic epic = (Epic) task;
            result = baseString + "," + "subtaskIds=" + epic.getSubtaskIds().toString().replace(",", ";");
        } else if (taskClassName.equals(TaskType.SUBTASK.toString())) {
            Subtask subtask = (Subtask) task;
            result = baseString + "," + "epicId=" + subtask.getEpicId();
        }

        return result;
    }

    private void save() {
        boolean fileExists = isFileExists();
        if (!fileExists) return;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.toPath().toString(), StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,task_type,title,task_status,description,additional_info" + "\n");

            for (Task value : getTasks().values()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }

            for (Epic value : getEpics().values()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }

            for (Subtask value : getSubtasks().values()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }
        } catch (IOException e) {
            try {
                throw new ManagerSaveException(e.getMessage());
            } catch (ManagerSaveException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Task createTask(String title, String description) {
        Task task = super.createTask(title, description);
        save();
        return task;
    }

    @Override
    public Epic createEpic(String title, String description) {
        Epic epic = super.createEpic(title, description);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String title, String description) {
        Subtask subtask = super.createSubtask(epicId, title, description);
        save();
        return subtask;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    @Override
    public void updateTaskStatusOfTask(int taskId, TaskStatus taskStatus) {
        super.updateTaskStatusOfTask(taskId, taskStatus);
        save();
    }

    @Override
    public void updateTaskStatusOfSubtask(int subtaskId, TaskStatus taskStatus) {
        super.updateTaskStatusOfSubtask(subtaskId, taskStatus);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void updateTask(int taskId, String title, String description) {
        super.updateTask(taskId, title, description);
        save();
    }

    @Override
    public void updateEpic(int epicId, String title, String description) {
        super.updateEpic(epicId, title, description);
        save();
    }

    @Override
    public void updateSubtask(int subtaskId, String title, String description) {
        super.updateSubtask(subtaskId, title, description);
        save();
    }
}
