package controllers;

import constants.TaskStatus;
import constants.TaskType;
import exceptions.CrossTaskException;
import exceptions.ManagerSaveException;
import models.Epic;
import models.Subtask;
import models.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public final class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) throws CrossTaskException {
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

    private void loadFromFile(File file) throws CrossTaskException {
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

                String taskType, title, taskStatus, description, subtaskIdsOrEpicId, startTime, duration;
                try {
                    taskType = split[1];
                    title = split[2];
                    taskStatus = split[3];
                    description = split[4];
                    subtaskIdsOrEpicId = split[5];
                    startTime = split[6];
                    duration = split[7];
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return;
                }


                if (taskType.equals(TaskType.TASK.toString())) {
                    Task task = null;
                    try {
                        task = createTask(id, title, description, LocalDateTime.parse(startTime), Duration.parse(duration));
                    } catch (CrossTaskException e) {
                        e.printStackTrace();
                    }

                    try {
                        task.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }

                    createId();
                } else if (taskType.equals(TaskType.EPIC.toString())) {
                    Epic epic = createEpic(id, title, description, startTime.equals("null") ? null : LocalDateTime.parse(startTime), duration.equals("null") ? null : Duration.parse(duration));

                    try {
                        epic.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        return;
                    }

                    String s;
                    try {
                        s = subtaskIdsOrEpicId.split("=")[1];
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
                        if (string.trim().isEmpty()) continue;
                        Integer num = null;
                        try {
                            num = Integer.parseInt(string.trim());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (num == null) return;

                        epic.addSubtask(num);
                    }

                    createId();
                } else if (taskType.equals(TaskType.SUBTASK.toString())) {
                    Subtask subtask = createSubtask(id, title, description, LocalDateTime.parse(startTime), Duration.parse(duration));

                    try {
                        subtask.setTaskStatus(TaskStatus.valueOf(taskStatus));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    String s;
                    try {
                        s = subtaskIdsOrEpicId.split("=")[1];
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

                    createId();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    public String toString(Task task) {
        if (task == null) {
            return null;
        }

        String taskClassName = task.getClass().getSimpleName().toUpperCase();
        String startString = task.getId() + "," + taskClassName + "," + task.getTitle() + "," + task.getTaskStatus() + "," + task.getDescription();
        String endString = task.getStartTime() + "," + task.getDuration();
        String result = null;

        if (taskClassName.equals(TaskType.TASK.toString())) {
            result = startString + "," + null + "," + endString;
        } else if (taskClassName.equals(TaskType.EPIC.toString())) {
            Epic epic = (Epic) task;
            result = startString + "," + "subtaskIds=" + epic.getSubtaskIds().toString().replace(",", ";") + "," + endString;
        } else if (taskClassName.equals(TaskType.SUBTASK.toString())) {
            Subtask subtask = (Subtask) task;
            result = startString + "," + "epicId=" + subtask.getEpicId() + "," + endString;
        }

        return result;
    }

    private void save() {
        boolean fileExists = isFileExists();
        if (!fileExists) return;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.toPath().toString(), StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,task_type,title,task_status,description,subtask_ids_or_epic_id,start_time,duration" + "\n");

            for (Task value : getTasks()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }

            for (Epic value : getEpics()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }

            for (Subtask value : getSubtasks()) {
                if (value != null) {
                    bufferedWriter.write(toString(value) + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task createTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Task task = super.createTask(taskId, title, description, startTime, duration);
        save();
        return task;
    }

    @Override
    public Epic createEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
        Epic epic = super.createEpic(epicId, title, description, startTime, duration);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(int epicId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Subtask subtask = super.createSubtask(epicId, title, description, startTime, duration);
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
    public Task updateTask(int taskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Task task = super.updateTask(taskId, title, description, startTime, duration);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(int epicId, String title, String description, LocalDateTime startTime, Duration duration) {
        Epic epic = super.updateEpic(epicId, title, description, startTime, duration);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(int subtaskId, String title, String description, LocalDateTime startTime, Duration duration) throws CrossTaskException {
        Subtask subtask = super.updateSubtask(subtaskId, title, description, startTime, duration);
        save();
        return subtask;
    }
}
