import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest {
    File tempFile;

    {
        try {
            tempFile = File.createTempFile("tempFile", ".csv");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    FileBackedTaskManager fBTM = new Managers().getDefaultFile(tempFile);

    @Test
    void testToString() {
        int id = 1;
        String title = "t";
        TaskStatus taskStatus = TaskStatus.NEW;
        String description = "d";
        LocalDateTime localDateTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(200);
        Task task = new Task(id, title, description, localDateTime, duration);
        task.setTaskStatus(taskStatus);
        String string = fBTM.toString(task);
        String[] split = string.split(",");
        Assertions.assertEquals(id, Integer.parseInt(split[0]));
        Assertions.assertEquals(TaskType.TASK.toString(), split[1]);
        Assertions.assertEquals(split[2], title);
        Assertions.assertEquals(split[3], taskStatus.toString());
        Assertions.assertEquals(split[4], description);
        Assertions.assertEquals(split[5], "null");
        Assertions.assertEquals(split[6], localDateTime.toString());
        Assertions.assertEquals(split[7], duration.toString());
    }

    @Override
    @Test
    void createTask() {
        String title = "t";
        String description = "d";
        Task task = fBTM.createTask(title, description, LocalDateTime.now(), Duration.ofMinutes(20));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine().split(",")[0];
                if (s.equals("id")) continue;
                Assertions.assertEquals(Integer.parseInt(s), task.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void createEpic() {
        String title = "t";
        String description = "d";
        Epic epic = fBTM.createEpic(title, description, null, null);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine().split(",")[0];
                if (s.equals("id")) continue;
                Assertions.assertEquals(Integer.parseInt(s), epic.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void createSubtask() {
        String title = "t";
        String description = "d";
        Epic epic = fBTM.createEpic(title, description, null, null);
        Subtask subtask = fBTM.createSubtask(epic.getId(), title, description, LocalDateTime.now(), Duration.ofMinutes(300));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.SUBTASK.toString())) {
                    Assertions.assertEquals(Integer.parseInt(split[0]), subtask.getId());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void deleteTask() {
        String title = "t";
        String description = "d";
        Task task = fBTM.createTask(title, description, LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertTrue(fBTM.getTasks().containsKey(task.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.TASK.toString())) {
                    Assertions.assertEquals(Integer.parseInt(split[0]), task.getId());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.deleteTask(task.getId());
        Assertions.assertFalse(fBTM.getTasks().containsKey(task.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void deleteEpic() {
        String title = "t";
        String description = "d";
        Epic epic = fBTM.createEpic(title, description, null, null);
        Assertions.assertTrue(fBTM.getEpics().containsKey(epic.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.EPIC.toString())) {
                    Assertions.assertEquals(Integer.parseInt(split[0]), epic.getId());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.deleteEpic(epic.getId());
        Assertions.assertFalse(fBTM.getEpics().containsKey(epic.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void deleteSubtask() {
        String title = "t";
        String description = "d";
        Epic epic = fBTM.createEpic(title, description, null, null);
        Subtask subtask = fBTM.createSubtask(epic.getId(), title, description, LocalDateTime.now(), Duration.ofMinutes(400));
        Assertions.assertTrue(fBTM.getSubtasks().containsKey(subtask.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.SUBTASK.toString())) {
                    Assertions.assertEquals(Integer.parseInt(split[0]), subtask.getId());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.deleteSubtask(subtask.getId());
        fBTM.deleteEpic(epic.getId());
        Assertions.assertFalse(fBTM.getSubtasks().containsKey(subtask.getId()));
        Assertions.assertFalse(fBTM.getEpics().containsKey(epic.getId()));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void updateTaskStatusOfTask() {
        String title = "t";
        String description = "d";
        Task task = fBTM.createTask(title, description, LocalDateTime.now(), Duration.ofMinutes(500));
        Assertions.assertEquals(task.getTaskStatus(), TaskStatus.NEW);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                Assertions.assertEquals(split[3], TaskStatus.NEW.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.updateTaskStatusOfTask(task.getId(), TaskStatus.DONE);
        Assertions.assertEquals(task.getTaskStatus(), TaskStatus.DONE);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                Assertions.assertEquals(split[3], TaskStatus.DONE.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void updateTaskStatusOfSubtask() {
        String title = "t";
        String description = "d";
        Epic epic = fBTM.createEpic(title, description, null, null);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.NEW);
        Subtask subtask = fBTM.createSubtask(epic.getId(), title, description, LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertEquals(subtask.getTaskStatus(), TaskStatus.NEW);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.NEW);
        Subtask subtask1 = fBTM.createSubtask(epic.getId(), title, description, LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(100));
        Assertions.assertEquals(subtask1.getTaskStatus(), TaskStatus.NEW);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.NEW);
        fBTM.updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(subtask.getTaskStatus(), TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        fBTM.updateTaskStatusOfSubtask(subtask1.getId(), TaskStatus.DONE);
        Assertions.assertEquals(subtask1.getTaskStatus(), TaskStatus.DONE);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        fBTM.updateTaskStatusOfSubtask(subtask.getId(), TaskStatus.DONE);
        Assertions.assertEquals(subtask.getTaskStatus(), TaskStatus.DONE);
        Assertions.assertEquals(epic.getTaskStatus(), TaskStatus.DONE);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.SUBTASK.toString())) {
                    Assertions.assertEquals(split[3], TaskStatus.DONE.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void clearTasks() {
        String title = "t";
        String description = "d";
        Assertions.assertTrue(fBTM.getTasks().isEmpty());
        Task task = fBTM.createTask(title, description, LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertFalse(fBTM.getTasks().isEmpty());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                Assertions.assertEquals(Integer.parseInt(split[0]), task.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.clearTasks();
        Assertions.assertTrue(fBTM.getTasks().isEmpty());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void clearEpics() {
        String title = "t";
        String description = "d";
        Assertions.assertTrue(fBTM.getEpics().isEmpty());
        Epic epic = fBTM.createEpic(title, description, null, null);
        Assertions.assertFalse(fBTM.getEpics().isEmpty());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                Assertions.assertEquals(Integer.parseInt(split[0]), epic.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.clearEpics();
        Assertions.assertTrue(fBTM.getEpics().isEmpty());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    void clearSubtasks() {
        String title = "t";
        String description = "d";
        Assertions.assertTrue(fBTM.getEpics().isEmpty());
        Epic epic = fBTM.createEpic(title, description, null, null);
        Subtask subtask = fBTM.createSubtask(epic.getId(), title, description, LocalDateTime.now(), Duration.ofMinutes(100));
        Assertions.assertFalse(fBTM.getSubtasks().isEmpty());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;

                if (split[1].equals(TaskType.SUBTASK.toString())) {
                    Assertions.assertEquals(Integer.parseInt(split[0]), subtask.getId());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fBTM.clearSubtasks();
        Assertions.assertTrue(fBTM.getSubtasks().isEmpty());
        fBTM.deleteEpic(epic.getId());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile, StandardCharsets.UTF_8))) {
            boolean flag = true;
            while (bufferedReader.ready()) {
                String[] split = bufferedReader.readLine().split(",");
                if (split[0].equals("id")) continue;
                flag = false;
            }

            Assertions.assertTrue(flag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}