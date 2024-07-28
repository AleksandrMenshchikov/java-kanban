public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask("t", "d");
        taskManager.createTask("t", "d");
        taskManager.updateTask(1, "t", "d");
        taskManager.deleteTask(3);
        taskManager.createTask("t", "d");
        taskManager.updateTaskStatusOfTask(2, TaskStatus.IN_PROGRESS);
        taskManager.updateTaskStatusOfTask(4, TaskStatus.IN_PROGRESS);
        taskManager.createEpic("t", "d");
        taskManager.createEpic("t", "d");
        taskManager.updateEpic(5, "t", "d");
        taskManager.deleteEpic(6);
        taskManager.createSubtask(7, "t", "d");
        taskManager.createSubtask(7, "t", "d");
        taskManager.deleteEpic(7);
        taskManager.createEpic("t", "d");
        taskManager.createSubtask(10, "t", "d");
        taskManager.createSubtask(10, "t", "d");
        taskManager.updateSubtask(11, "t", "d");
        taskManager.deleteSubtask(12);
        taskManager.createSubtask(10, "t", "d");
        taskManager.updateTaskStatusOfSubtask(13, TaskStatus.DONE);
        taskManager.updateTaskStatusOfSubtask(14, TaskStatus.DONE);
        taskManager.updateSubtask(13, "t", "d");
        taskManager.updateTaskStatusOfSubtask(14, TaskStatus.DONE);
        taskManager.updateTaskStatusOfSubtask(15, TaskStatus.DONE);
        taskManager.printAllData();
    }
}
