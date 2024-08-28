public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new Managers().getDefault();
        taskManager.createTask("t", "d");
        taskManager.createTask("t", "d");
        taskManager.createEpic("t", "d");
        taskManager.createSubtask(3, "t", "d");
        taskManager.createSubtask(3, "t", "d");
        taskManager.createSubtask(3, "t", "d");
        taskManager.createEpic("t", "d");
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);
        taskManager.deleteTask(1);
        taskManager.deleteTask(2);
        taskManager.deleteSubtask(4);
        taskManager.deleteSubtask(5);
        taskManager.deleteSubtask(6);
        taskManager.deleteEpic(3);
        taskManager.deleteEpic(7);

        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
