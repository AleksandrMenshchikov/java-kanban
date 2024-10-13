package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import constants.RequestMethod;
import constants.StatusCode;
import models.Task;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public final class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("/prioritized/?", path)) {
            if (requestMethod.equals(RequestMethod.GET.toString())) {
                List<Task> history = taskManager.getPrioritizedTasks();
                sendOk(exchange, history, requestMethod, path);
            } else {
                sendMethodNotAllowed(exchange, requestMethod, path, StatusCode.METHOD_NOT_ALLOWED.getText());
            }
        } else {
            sendNotFound(exchange, requestMethod, path, StatusCode.NOT_FOUND.getText());
        }
    }
}
