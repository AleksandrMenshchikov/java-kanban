import com.sun.net.httpserver.HttpServer;
import controllers.EpicHandler;
import controllers.HistoryHandler;
import controllers.PrioritizedHandler;
import controllers.SubtaskHandler;
import controllers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(0);
    }
}
