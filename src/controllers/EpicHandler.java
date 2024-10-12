package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.StatusCode;
import models.Epic;
import models.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public final class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("/epics/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                sendOk(exchange, taskManager.getEpics(), requestMethod, path);
            } else if (requestMethod.equals(RequestMethod.POST.toString())) {
                try (InputStream requestBody = exchange.getRequestBody()) {
                    byte[] bytes = requestBody.readAllBytes();
                    String s = new String(bytes, StandardCharsets.UTF_8);

                    if (!JsonParser.parseString(s).isJsonObject()) {
                        sendBadRequest(exchange, requestMethod, path, "Тело запроса должно быть объектом");
                        return;
                    }

                    JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
                    JsonElement title = jsonObject.get("title");
                    JsonElement description = jsonObject.get("description");

                    if (title == null || description == null) {
                        sendBadRequest(exchange, requestMethod, path, "Тело запроса должно состоять из полей: title, description");
                        return;
                    }

                    Epic epic = taskManager.createEpic(
                            taskManager.createId(),
                            title.getAsString(),
                            description.getAsString(),
                            null,
                            null
                    );

                    if (epic != null) {
                        sendCreated(exchange, epic, requestMethod, path);
                    }
                }
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else if (Pattern.matches("/epics/\\d+/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                String id = path.split("/")[2];
                Epic epic = taskManager.getEpicById(Integer.parseInt(id));

                if (epic == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    sendOk(exchange, epic, requestMethod, path);
                }
            } else if (requestMethod.equals(RequestMethod.DELETE.toString())) {
                String id = path.split("/")[2];
                Epic epic = taskManager.getEpicById(Integer.parseInt(id));

                if (epic == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    taskManager.deleteEpic(Integer.parseInt(id));
                    sendOk(exchange, epic, requestMethod, path);
                }
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else if (Pattern.matches("/epics/\\d+/subtasks/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                String id = path.split("/")[3];
                Epic epic = taskManager.getEpicById(Integer.parseInt(id));

                if (epic == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                    return;
                }

                List<Integer> subtaskIds = epic.getSubtaskIds();
                List<Subtask> list = taskManager.getSubtasks().stream().filter(subtask -> subtaskIds.contains(subtask.getId())).toList();
                sendOk(exchange, list, requestMethod, path);
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else {
            sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
        }
    }
}
