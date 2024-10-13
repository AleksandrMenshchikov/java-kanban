package controllers;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import adapters.TaskStatusAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import constants.StatusCode;
import constants.TaskStatus;
import models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseHttpHandler {
    public static final TaskManager taskManager = new Managers().getDefault();
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(TaskStatus.class, new TaskStatusAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private final Map<String, Object> map = new HashMap<>();

    public static Gson getGson() {
        return gson;
    }

    private byte[] createResponse(Object data, int statusCode, String requestMethod, String path, String messageError) {
        map.clear();
        map.put("data", data);
        map.put("statusCode", statusCode);
        map.put("requestMethod", requestMethod);
        map.put("path", path);
        map.put("messageError", messageError);
        return gson.toJson(map).getBytes(StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, byte[] res) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, res.length);

        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(res);
        }
    }

    protected final void sendOk(HttpExchange exchange, Object data, String requestMethod, String path) throws IOException {
        byte[] res = createResponse(data, StatusCode.OK.getCode(), requestMethod, path, null);
        sendResponse(exchange, StatusCode.OK.getCode(), res);
    }

    protected final void sendCreated(HttpExchange exchange, Task data, String requestMethod, String path) throws IOException {
        byte[] res = createResponse(data, StatusCode.CREATED.getCode(), requestMethod, path, null);
        sendResponse(exchange, StatusCode.CREATED.getCode(), res);
    }

    protected final void sendBadRequest(HttpExchange exchange, String requestMethod, String path, String message) throws IOException {
        byte[] res = createResponse(null, StatusCode.BAD_REQUEST.getCode(), requestMethod, path, message);
        sendResponse(exchange, StatusCode.BAD_REQUEST.getCode(), res);
    }

    protected final void sendNotFound(HttpExchange exchange, String requestMethod, String path, String message) throws IOException {
        byte[] res = createResponse(null, StatusCode.NOT_FOUND.getCode(), requestMethod, path, message);
        sendResponse(exchange, StatusCode.NOT_FOUND.getCode(), res);
    }

    protected final void sendMethodNotAllowed(HttpExchange exchange, String requestMethod, String path, String message) throws IOException {
        byte[] res = createResponse(null, StatusCode.METHOD_NOT_ALLOWED.getCode(), requestMethod, path, message);
        sendResponse(exchange, StatusCode.METHOD_NOT_ALLOWED.getCode(), res);
    }

    protected final void sendNotAcceptable(HttpExchange exchange, String requestMethod, String path, String message) throws IOException {
        byte[] res = createResponse(null, StatusCode.NOT_ACCEPTABLE.getCode(), requestMethod, path, message);
        sendResponse(exchange, StatusCode.NOT_ACCEPTABLE.getCode(), res);
    }
}
