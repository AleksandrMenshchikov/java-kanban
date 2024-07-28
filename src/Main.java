public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("t", "d"));
        taskManager.createTask(new Task("t", "d"));
        taskManager.updateTask(1, new Task("t", "d"));
        taskManager.deleteTask(3);
        taskManager.createTask(new Task("t", "d"));
        taskManager.updateTaskStatusOfTask(2, TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatusOfTask(4, TaskStatus.IN_PROGRESS);
        taskManager.createEpic(new Epic("t", "d"));
        taskManager.createEpic(new Epic("t", "d"));
        taskManager.updateEpic(5, new Epic("t", "d"));
        taskManager.deleteEpic(6);
        taskManager.createSubtask(7, new Subtask("t", "d"));
        taskManager.createSubtask(7, new Subtask("t", "d"));
        taskManager.deleteEpic(7);
        taskManager.createEpic(new Epic("t", "d"));
        taskManager.createSubtask(10, new Subtask("t", "d"));
        taskManager.createSubtask(10, new Subtask("t", "d"));
        taskManager.updateSubtask(11, new Subtask("t", "d"));
        taskManager.deleteSubtask(12);
        taskManager.createSubtask(10, new Subtask("t", "d"));
        taskManager.updateTaskStatusOfSubtask(13, TaskStatus.DONE);
        taskManager.updateTaskStatusOfSubtask(14, TaskStatus.DONE);
        taskManager.updateSubtask(13, new Subtask("t", "d"));
        taskManager.updateTaskStatusOfSubtask(14, TaskStatus.DONE);
        taskManager.updateTaskStatusOfSubtask(15, TaskStatus.DONE);
        taskManager.printAllData();
    }
}
