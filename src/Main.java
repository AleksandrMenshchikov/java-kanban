public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.putTask(new Task("t", "d"));
        taskManager.putTask(new Task("t", "d"));
        taskManager.putEpic(new Epic("t", "d"));
        taskManager.putSubtask(3, new Subtask("t", "d"));
        taskManager.putSubtask(3, new Subtask("t", "d"));
        taskManager.putEpic(new Epic("t", "d"));
        taskManager.putSubtask(6, new Subtask("t", "d"));
        taskManager.setTaskStatusForTask(1, TaskStatus.NEW);
        taskManager.setTaskStatusForTask(2, TaskStatus.IN_PROGRESS);
        taskManager.setTaskStatusForSubtask(4, TaskStatus.NEW);
        taskManager.setTaskStatusForSubtask(5, TaskStatus.DONE);
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(3);
        taskManager.removeSubtaskById(7);
        taskManager.printAllData();
    }
}
