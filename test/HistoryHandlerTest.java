import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import constants.RequestMethod;
import exceptions.CrossTaskException;
import models.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class HistoryHandlerTest extends BaseHandlerTest {

    @Test
    void handle() throws IOException, InterruptedException, CrossTaskException {
        // GET /HISTORY
        // получаем []
        HttpResponse<String> response = getResponse("/history", RequestMethod.GET.toString(), null);
        JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertEquals(200, response.statusCode());

        Epic epic = taskManager.createEpic(taskManager.createId(), "t", "d", LocalDateTime.now().plusMinutes(310), Duration.ofMinutes(30));
        taskManager.updateEpic(epic.getId(), "t", "d", LocalDateTime.now().plusMinutes(400), Duration.ofMinutes(30));

        // GET /HISTORY
        // получаем [Epic]
        HttpResponse<String> response1 = getResponse("/history", RequestMethod.GET.toString(), null);
        JsonArray data1 = JsonParser.parseString(response1.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertFalse(data1.isEmpty());
        Assertions.assertEquals(200, response1.statusCode());
    }
}