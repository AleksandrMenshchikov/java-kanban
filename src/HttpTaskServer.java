import com.sun.net.httpserver.HttpServer;
import controllers.EpicHandler;
import controllers.HistoryHandler;
import controllers.Managers;
import controllers.PrioritizedHandler;
import controllers.SubtaskHandler;
import controllers.TaskHandler;
import controllers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final TaskManager taskManager = new Managers().getDefault();
    static private HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(0);
    }
}
