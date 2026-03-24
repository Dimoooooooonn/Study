package sanatorium;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    public static Map<String, String> parseForm(HttpExchange exchange) throws IOException {
        String body = readBody(exchange.getRequestBody());
        return parseParams(body);
    }

    public static Map<String, String> parseQuery(String query) {
        return parseParams(query);
    }

    private static Map<String, String> parseParams(String text) {
        Map<String, String> map = new HashMap<>();
        if (text == null || text.isBlank()) {
            return map;
        }
        String[] pairs = text.split("&");
        for (String pair : pairs) {
            if (pair.isBlank()) {
                continue;
            }
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length > 1 ? decode(parts[1]) : "";
            map.put(key, value);
        }
        return map;
    }

    public static String readBody(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void sendHtml(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    public static void sendBytes(HttpExchange exchange, byte[] bytes, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    public static void sendNotFound(HttpExchange exchange) throws IOException {
        String text = "404 - страница не найдена";
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(404, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    public static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    public static String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    public static String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }

    public static String getCookie(HttpExchange exchange, String name) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies == null) {
            return "";
        }
        for (String header : cookies) {
            String[] parts = header.split(";");
            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.startsWith(name + "=")) {
                    return trimmed.substring(name.length() + 1);
                }
            }
        }
        return "";
    }

    public static byte[] readFile(Path file) throws IOException {
        return Files.readAllBytes(file);
    }

    public static String contentType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".html")) return "text/html; charset=UTF-8";
        if (name.endsWith(".css")) return "text/css; charset=UTF-8";
        if (name.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".webp")) return "image/webp";
        return "application/octet-stream";
    }
}
