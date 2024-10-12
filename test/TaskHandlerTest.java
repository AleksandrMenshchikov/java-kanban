import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.RequestMethod;
import exceptions.CrossTaskException;
import models.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

class TaskHandlerTest extends BaseHandlerTest {

    @Test
    void handle() throws IOException, InterruptedException, CrossTaskException {
        // GET /TASKS
        // получаем []
        HttpResponse<String> response = getResponse("/tasks", RequestMethod.GET.toString(), null);
        JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data.isEmpty());
        Assertions.assertEquals(200, response.statusCode());

        // GET POST PUT DELETE /TASKSQ
        // получаем 404 Ресурс не найден
        HttpResponse<String> response3 = getResponse("/tasksq", RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response3.statusCode());
        HttpResponse<String> response4 = getResponse("/tasksq", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(404, response4.statusCode());
        HttpResponse<String> response5 = getResponse("/tasksq", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(404, response5.statusCode());
        HttpResponse<String> response6 = getResponse("/tasksq", RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response6.statusCode());

        // POST /TASKS
        // создаем Task
        String json = gson.toJson(Map.of(
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now().plusMinutes(30),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response1 = getResponse("/tasks", RequestMethod.POST.toString(), json);
        Assertions.assertEquals(201, response1.statusCode());
        JsonObject jsonObject1 = JsonParser.parseString(response1.body()).getAsJsonObject().get("data").getAsJsonObject();
        Task task = gson.fromJson(jsonObject1, Task.class);
        Assertions.assertNotNull(task);

        // PUT /TASKS
        // получаем 406 Задача пересекается с существующими
        String json2 = gson.toJson(Map.of(
                "id", task.getId(),
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response15 = getResponse("/tasks", RequestMethod.PUT.toString(), json2);
        Assertions.assertEquals(406, response15.statusCode());

        // POST /TASKS
        // получаем 406 Задача пересекается с существующими
        String json1 = gson.toJson(Map.of(
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response14 = getResponse("/tasks", RequestMethod.POST.toString(), json);
        Assertions.assertEquals(406, response14.statusCode());

        // GET /TASKS
        // получаем [Task]
        HttpResponse<String> response2 = getResponse("/tasks", RequestMethod.GET.toString(), null);
        JsonElement jsonElement1 = JsonParser.parseString(response2.body());
        Assertions.assertEquals(200, response2.statusCode());
        Assertions.assertTrue(jsonElement1.isJsonObject());
        JsonElement jsonElement2 = jsonElement1.getAsJsonObject().get("data").getAsJsonArray().get(0);
        Task task1 = gson.fromJson(jsonElement2, Task.class);
        Assertions.assertNotNull(task1);
        Task task3 = taskManager.getTasks().getFirst();

        // GET /TASKS/{ID}
        // получаем [Task]
        HttpResponse<String> response7 = getResponse("/tasks/" + task3.getId(), RequestMethod.GET.toString(), null);
        JsonElement jsonElement3 = JsonParser.parseString(response7.body()).getAsJsonObject().get("data").getAsJsonObject();
        Task task2 = gson.fromJson(jsonElement3, Task.class);
        Assertions.assertNotNull(task2);

        // GET  /TASKS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response8 = getResponse("/tasks/" + task3.getId() + 1, RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response8.statusCode());

        // POST PUT /TASKS/2
        // получаем 405 Метод не применяется
        HttpResponse<String> response9 = getResponse("/tasks/2", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(405, response9.statusCode());
        HttpResponse<String> response10 = getResponse("/tasks/2", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(405, response10.statusCode());

        // DELETE /TASKS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response11 = getResponse("/tasks/" + task3.getId() + 1, RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response11.statusCode());

        // DELETE /TASKS/{ID}
        // удаляет Task
        HttpResponse<String> response12 = getResponse("/tasks/" + task3.getId(), RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(200, response12.statusCode());

        // GET /TASKS
        // получаем []
        HttpResponse<String> response13 = getResponse("/tasks", RequestMethod.GET.toString(), null);
        JsonArray data1 = JsonParser.parseString(response13.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data1.isEmpty());
        Assertions.assertEquals(200, response13.statusCode());
    }
}