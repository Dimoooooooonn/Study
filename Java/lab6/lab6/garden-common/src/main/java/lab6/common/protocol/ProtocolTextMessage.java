package lab6.common.protocol;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProtocolTextMessage {
    private final LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private final List<String> deviceLines = new ArrayList<>();

    public void put(String key, String value) {
        headers.put(key, value == null ? "" : value);
    }

    public String get(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addDeviceLine(String deviceLine) {
        if (deviceLine != null && !deviceLine.isBlank()) {
            deviceLines.add(deviceLine);
        }
    }

    public List<String> getDeviceLines() {
        return deviceLines;
    }

    public byte[] toBytes() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.append(entry.getKey()).append('=').append(escape(entry.getValue())).append('\n');
        }
        builder.append('\n');
        for (String deviceLine : deviceLines) {
            builder.append("device=").append(escape(deviceLine)).append('\n');
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static ProtocolTextMessage fromBytes(byte[] bytes) {
        return fromString(new String(bytes, StandardCharsets.UTF_8));
    }

    public static ProtocolTextMessage fromString(String content) {
        ProtocolTextMessage message = new ProtocolTextMessage();
        String[] lines = content.split("\\r?\\n", -1);
        boolean body = false;

        for (String line : lines) {
            if (!body && line.isEmpty()) {
                body = true;
                continue;
            }
            if (line.isBlank()) {
                continue;
            }

            int index = line.indexOf('=');
            if (index <= 0) {
                continue;
            }

            String key = line.substring(0, index).trim();
            String value = unescape(line.substring(index + 1));

            if (body && "device".equals(key)) {
                message.addDeviceLine(value);
            } else {
                message.put(key, value);
            }
        }
        return message;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\n", "\\n");
    }

    private static String unescape(String value) {
        StringBuilder builder = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (escaping) {
                if (current == 'n') {
                    builder.append('\n');
                } else {
                    builder.append(current);
                }
                escaping = false;
            } else if (current == '\\') {
                escaping = true;
            } else {
                builder.append(current);
            }
        }
        if (escaping) {
            builder.append('\\');
        }
        return builder.toString();
    }
}
