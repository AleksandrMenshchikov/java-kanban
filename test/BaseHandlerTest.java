import com.google.gson.Gson;
import constants.RequestMethod;
import controllers.BaseHttpHandler;
import controllers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class BaseHandlerTest {
    protected final Gson gson = BaseHttpHandler.getGson();
    protected TaskManager taskManager = HttpTaskServer.taskManager;

    @BeforeEach
    void setUp() throws IOException {
        HttpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskManager.clearTasks();
        taskManager.clearEpics();
        taskManager.clearSubtasks();
        HttpTaskServer.stop();
    }

    final HttpResponse<String> getResponse(String path, String requestMethod, String jsonData) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest.Builder builder = HttpRequest.newBuilder();
            URI uri = URI.create("http://localhost:8080" + path);

            if (requestMethod.equals(RequestMethod.GET.toString())) {
                builder.GET();
            } else if (requestMethod.equals(RequestMethod.POST.toString())) {
                builder.POST(HttpRequest.BodyPublishers.ofString(jsonData));
            } else if (requestMethod.equals(RequestMethod.PUT.toString())) {
                builder.PUT(HttpRequest.BodyPublishers.ofString(jsonData));
            } else if (requestMethod.equals(RequestMethod.DELETE.toString())) {
                builder.DELETE();
            }

            HttpRequest httpRequest = builder.uri(uri).version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json;charset=utf-8").build();
            return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }
    }
}
