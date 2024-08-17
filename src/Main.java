public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new Managers().getDefault();
        taskManager.createTask("t", "d");
        taskManager.createEpic("t", "d");
        taskManager.createSubtask(2, "t", "d");
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
