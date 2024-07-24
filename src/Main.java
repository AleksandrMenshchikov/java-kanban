public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.setTask(new Task(ID.getId(), "t", "d"));
        taskManager.setTask(new Task(ID.getId(), "t", "d"));
        taskManager.setEpic(1, new Epic(ID.getId(), "t", "d"));
        taskManager.setSubtask(1, 3, new Subtask(ID.getId(), "t", "d"));
        taskManager.setSubtask(1, 3, new Subtask(ID.getId(), "t", "d"));
        taskManager.setEpic(2, new Epic(ID.getId(), "t", "d"));
        taskManager.setSubtask(2, 6, new Subtask(ID.getId(), "t", "d"));
        taskManager.setTaskStatusForTask(1, TaskStatus.NEW);
        taskManager.setTaskStatusForSubtask(1, 3,4, TaskStatus.DONE);
        taskManager.deleteTaskById(1);
        taskManager.deleteEpicById(2, 6);
        taskManager.printData();
    }
}
