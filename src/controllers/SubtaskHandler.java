package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.StatusCode;
import exceptions.CrossTaskException;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("/subtasks/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                sendOk(exchange, taskManager.getSubtasks(), requestMethod, path);
            } else if (requestMethod.equals(RequestMethod.POST.toString()) || requestMethod.equals(RequestMethod.PUT.toString())) {
                try (InputStream requestBody = exchange.getRequestBody()) {
                    byte[] bytes = requestBody.readAllBytes();
                    String s = new String(bytes, StandardCharsets.UTF_8);

                    if (!JsonParser.parseString(s).isJsonObject()) {
                        sendBadRequest(exchange, requestMethod, path, "Тело запроса должно быть объектом");
                        return;
                    }

                    JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
                    JsonElement id = jsonObject.get("id");
                    JsonElement title = jsonObject.get("title");
                    JsonElement description = jsonObject.get("description");
                    JsonElement startTime = jsonObject.get("startTime");
                    JsonElement duration = jsonObject.get("duration");

                    if (title == null || description == null || startTime == null || duration == null || id == null) {
                        sendBadRequest(exchange, requestMethod, path, "Тело запроса должно состоять из полей: id, title, description, startTime, duration");
                        return;
                    }

                    if (requestMethod.equals(RequestMethod.POST.toString())) {
                        try {
                            Task task = taskManager.createSubtask(
                                    id.getAsInt(),
                                    title.getAsString(),
                                    description.getAsString(),
                                    LocalDateTime.parse(startTime.getAsString()),
                                    Duration.parse(duration.getAsString())
                            );

                            if (task != null) {
                                sendCreated(exchange, task, requestMethod, path);
                            } else {
                                sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                            }
                        } catch (CrossTaskException e) {
                            sendNotAcceptable(exchange, requestMethod, path, e.getMessage());
                        }
                    } else {
                        try {
                            Task task = taskManager.updateSubtask(
                                    id.getAsInt(),
                                    title.getAsString(),
                                    description.getAsString(),
                                    LocalDateTime.parse(startTime.getAsString()),
                                    Duration.parse(duration.getAsString())
                            );

                            if (task != null) {
                                sendCreated(exchange, task, requestMethod, path);
                            } else {
                                sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                            }
                        } catch (CrossTaskException e) {
                            sendNotAcceptable(exchange, requestMethod, path, e.getMessage());
                        }
                    }
                }
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else if (Pattern.matches("/subtasks/\\d+/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                String id = path.split("/")[2];
                Subtask task = taskManager.getSubtaskById(Integer.parseInt(id));

                if (task == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    sendOk(exchange, task, requestMethod, path);
                }
            } else if (requestMethod.equals(RequestMethod.DELETE.toString())) {
                String id = path.split("/")[2];
                Subtask task = taskManager.getSubtaskById(Integer.parseInt(id));

                if (task == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    taskManager.deleteSubtask(Integer.parseInt(id));
                    sendOk(exchange, task, requestMethod, path);
                }
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else {
            sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
        }
    }
}
