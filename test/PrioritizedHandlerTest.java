import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import constants.RequestMethod;
import exceptions.CrossTaskException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class PrioritizedHandlerTest extends BaseHandlerTest {

    @Test
    void handle() throws IOException, InterruptedException, CrossTaskException {
        // GET /PRIORITIZED
        // получаем []
        HttpResponse<String> response = getResponse("/prioritized", RequestMethod.GET.toString(), null);
        JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertTrue(data.isEmpty());
        Assertions.assertEquals(200, response.statusCode());

        taskManager.createTask(taskManager.createId(), "t", "d", LocalDateTime.now().plusMinutes(310), Duration.ofMinutes(30));

        // GET /PRIORITIZED
        // получаем [Epic]
        HttpResponse<String> response1 = getResponse("/prioritized", RequestMethod.GET.toString(), null);
        JsonArray data1 = JsonParser.parseString(response1.body()).getAsJsonObject().get("data").getAsJsonArray();
        Assertions.assertFalse(data1.isEmpty());
        Assertions.assertEquals(200, response1.statusCode());
    }
}