import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.RequestMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

class EpicHandlerTest extends BaseHandlerTest {

    @Test
    void handle() throws IOException, InterruptedException {
        // GET /EPICS
        // получаем []
        HttpResponse<String> response = getResponse("/epics", RequestMethod.GET.toString(), null);
        JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data.isEmpty());
        Assertions.assertEquals(200, response.statusCode());

        // GET POST PUT DELETE /EPICSQ
        // получаем 404 Ресурс не найден
        HttpResponse<String> response1 = getResponse("/epicsq", RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response1.statusCode());
        HttpResponse<String> response2 = getResponse("/epicsq", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(404, response2.statusCode());
        HttpResponse<String> response3 = getResponse("/epicsq", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(404, response3.statusCode());
        HttpResponse<String> response4 = getResponse("/epicsq", RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response4.statusCode());

        // POST /EPICS
        // создаем Epic
        String json = gson.toJson(Map.of("title", "t", "description", "d"));
        HttpResponse<String> response5 = getResponse("/epics", RequestMethod.POST.toString(), json);
        Assertions.assertEquals(201, response5.statusCode());
        JsonObject jsonObject = JsonParser.parseString(response5.body()).getAsJsonObject().get("data").getAsJsonObject();
        Assertions.assertFalse(jsonObject.isJsonNull());

        // GET /EPICS
        // получаем [Epic]
        HttpResponse<String> response6 = getResponse("/epics", RequestMethod.GET.toString(), null);
        JsonElement jsonElement = JsonParser.parseString(response6.body());
        Assertions.assertEquals(200, response6.statusCode());
        Assertions.assertTrue(jsonElement.isJsonObject());
        JsonElement jsonElement1 = jsonElement.getAsJsonObject().get("data").getAsJsonArray().get(0);
        Assertions.assertFalse(jsonElement1.isJsonNull());
        int id = jsonElement1.getAsJsonObject().get("id").getAsInt();

        // GET  /EPICS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response7 = getResponse("/epics/" + id + 1, RequestMethod.GET.toString(), null);
        Assertions.assertEquals(404, response7.statusCode());

        // POST PUT /EPICS/2
        // получаем 405 Метод не применяется
        HttpResponse<String> response9 = getResponse("/epics/2", RequestMethod.POST.toString(), "");
        Assertions.assertEquals(405, response9.statusCode());
        HttpResponse<String> response10 = getResponse("/epics/2", RequestMethod.PUT.toString(), "");
        Assertions.assertEquals(405, response10.statusCode());

        // DELETE /EPICS/{ID}
        // получаем 404 Ресурс не найден
        HttpResponse<String> response11 = getResponse("/epics/" + id + 1, RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(404, response11.statusCode());

        // DELETE /EPICS/{ID}
        // удаляет Epic
        HttpResponse<String> response12 = getResponse("/epics/" + id, RequestMethod.DELETE.toString(), null);
        Assertions.assertEquals(200, response12.statusCode());

        // GET /EPICS
        // получаем []
        HttpResponse<String> response13 = getResponse("/epics", RequestMethod.GET.toString(), null);
        JsonArray data1 = JsonParser.parseString(response13.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data1.isEmpty());
        Assertions.assertEquals(200, response13.statusCode());
    }
}