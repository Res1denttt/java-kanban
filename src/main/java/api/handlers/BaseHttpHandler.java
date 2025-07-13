package api.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {
    protected void sendText(HttpExchange exchange, String text) {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (exchange) {
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendNotFound(HttpExchange exchange) {
        try (exchange) {
            exchange.sendResponseHeaders(404, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendHasInteractions(HttpExchange exchange) {
        try (exchange) {
            exchange.sendResponseHeaders(406, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendInternalServerError(HttpExchange exchange) {
        try (exchange) {
            exchange.sendResponseHeaders(500, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
