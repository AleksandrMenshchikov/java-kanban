package controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.StatusCode;
import exceptions.CrossTaskException;
import models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("/tasks/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                sendOk(exchange, taskManager.getTasks(), requestMethod, path);
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

                    if (title == null || description == null || startTime == null || duration == null) {
                        sendBadRequest(exchange, requestMethod, path, "Тело запроса должно состоять из полей: id(при обновлении задачи), title, description, startTime, duration");
                        return;
                    }

                    if (requestMethod.equals(RequestMethod.POST.toString())) {
                        try {
                            Task task = taskManager.createTask(
                                    taskManager.createId(),
                                    title.getAsString(),
                                    description.getAsString(),
                                    LocalDateTime.parse(startTime.getAsString()),
                                    Duration.parse(duration.getAsString())
                            );
                            sendCreated(exchange, task, requestMethod, path);
                        } catch (CrossTaskException e) {
                            sendNotAcceptable(exchange, requestMethod, path, e.getMessage());
                        }
                    } else if (requestMethod.equals(RequestMethod.PUT.toString())) {
                        if (id == null) {
                            sendBadRequest(exchange, requestMethod, path, "Тело запроса должно состоять из полей: id(при обновлении задачи), title, description, startTime, duration");
                            return;
                        }

                        Task task = taskManager.getTaskById(id.getAsInt());

                        if (task == null) {
                            sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                        } else {
                            try {
                                Task task1 = taskManager.updateTask(
                                        task.getId(),
                                        title.getAsString(),
                                        description.getAsString(),
                                        LocalDateTime.parse(startTime.getAsString()),
                                        Duration.parse(duration.getAsString())
                                );
                                sendCreated(exchange, task1, requestMethod, path);
                            } catch (CrossTaskException e) {
                                sendNotAcceptable(exchange, requestMethod, path, e.getMessage());
                            }
                        }
                    }
                }
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else if (Pattern.matches("/tasks/\\d+/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                String id = path.split("/")[2];
                Task task = taskManager.getTaskById(Integer.parseInt(id));

                if (task == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    sendOk(exchange, task, requestMethod, path);
                }
            } else if (requestMethod.equals(RequestMethod.DELETE.toString())) {
                String id = path.split("/")[2];
                Task task = taskManager.getTaskById(Integer.parseInt(id));

                if (task == null) {
                    sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
                } else {
                    taskManager.deleteTask(Integer.parseInt(id));
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
