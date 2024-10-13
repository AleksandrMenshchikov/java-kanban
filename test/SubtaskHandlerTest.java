import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.RequestMethod;
import models.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

class SubtaskHandlerTest extends BaseHandlerTest {

    @Test
    void handle() throws IOException, InterruptedException {
        // GET / SUBTASKS
        // получаем []
        HttpResponse<String> response = getResponse("/subtasks", RequestMethod.GET.toString(), null);
        JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data.isEmpty());
        Assertions.assertEquals(200, response.statusCode());

        // GET POST PUT DELETE /SUBTASKSQ
        // получаем 404 Ресурс не найден
        HttpResponse<String> response1 = getResponse("/subtasksq", RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response1.statusCode());
        HttpResponse<String> response2 = getResponse("/subtasksq", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(404, response2.statusCode());
        HttpResponse<String> response3 = getResponse("/subtasksq", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(404, response3.statusCode());
        HttpResponse<String> response4 = getResponse("/subtasksq", RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response4.statusCode());

        // POST /SUBTASKS
        // создаем Subtask
        String json1 = gson.toJson(Map.of(
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response6 = getResponse("/epics", RequestMethod.POST.toString(), json1);
        int id = JsonParser.parseString(response6.body()).getAsJsonObject().get("data").getAsJsonObject().get("id").getAsInt();
        String json = gson.toJson(Map.of(
                "id", id,
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response5 = getResponse("/subtasks", RequestMethod.POST.toString(), json);
        Assertions.assertEquals(201, response5.statusCode());
        JsonObject jsonObject1 = JsonParser.parseString(response5.body()).getAsJsonObject().get("data").getAsJsonObject();
        Subtask subtask = gson.fromJson(jsonObject1, Subtask.class);
        Assertions.assertNotNull(subtask);

        // PUT /SUBTASKS
        // получаем 406 Задача пересекается с существующими
        String json2 = gson.toJson(Map.of(
                "id", subtask.getId(),
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response7 = getResponse("/subtasks", RequestMethod.PUT.toString(), json2);
        Assertions.assertEquals(406, response7.statusCode());

        // POST /SUBTASKS
        // получаем 406 Задача пересекается с существующими
        String json3 = gson.toJson(Map.of(
                "id", id,
                "title", "t",
                "description", "d",
                "startTime", LocalDateTime.now(),
                "duration", Duration.ofMinutes(30))
        );
        HttpResponse<String> response8 = getResponse("/subtasks", RequestMethod.POST.toString(), json3);
        Assertions.assertEquals(406, response8.statusCode());

        // GET /SUBTASKS
        // получаем [Subtask]
        HttpResponse<String> response9 = getResponse("/subtasks", RequestMethod.GET.toString(), null);
        JsonElement jsonElement = JsonParser.parseString(response9.body()).getAsJsonObject().get("data").getAsJsonArray().get(0);
        Assertions.assertEquals(200, response9.statusCode());
        Assertions.assertTrue(jsonElement.isJsonObject());
        Subtask subtask1 = gson.fromJson(jsonElement, Subtask.class);
        Assertions.assertNotNull(subtask1);

        // GET /SUBTASKS/{ID}
        // получаем [Subtask]
        HttpResponse<String> response10 = getResponse("/subtasks/" + subtask1.getId(), RequestMethod.GET.toString(), null);
        JsonObject jsonObject = JsonParser.parseString(response10.body()).getAsJsonObject().get("data").getAsJsonObject();
        Subtask subtask2 = gson.fromJson(jsonObject, Subtask.class);
        Assertions.assertNotNull(subtask2);

        // GET  /SUBTASKS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response11 = getResponse("/subtasks/" + subtask1.getId() + 1, RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response11.statusCode());

        // POST PUT /SUBTASKS/2
        // получаем 405 Метод не применяется
        HttpResponse<String> response12 = getResponse("/subtasks/2", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(405, response12.statusCode());
        HttpResponse<String> response13 = getResponse("/subtasks/2", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(405, response13.statusCode());

        // DELETE /SUBTASKS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response14 = getResponse("/subtasks/" + subtask1.getId() + 1, RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response14.statusCode());

        // DELETE /SUBTASKS/{ID}
        // удаляет Subtask
        HttpResponse<String> response15 = getResponse("/subtasks/" + subtask1.getId(), RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(200, response15.statusCode());

        // GET /SUBTASKS
        // получаем []
        HttpResponse<String> response16 = getResponse("/subtasks", RequestMethod.GET.toString(), null);
        JsonArray data1 = JsonParser.parseString(response16.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data1.isEmpty());
        Assertions.assertEquals(200, response16.statusCode());
    }
}