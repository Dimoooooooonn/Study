package lab6.server.network;

import lab6.common.dto.DeviceSnapshot;
import lab6.common.protocol.ProtocolTextMessage;
import lab6.server.service.DeviceService;
import lab6.server.service.OperationResult;

public class RequestProcessor {
    private final DeviceService deviceService;

    public RequestProcessor(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public ProtocolTextMessage process(ProtocolTextMessage request) {
        String command = request.get("command");
        OperationResult result;

        if (command == null) {
            return errorResponse("Не указана команда клиента");
        }

        switch (command) {
            case "SHOW_ONCE" -> result = deviceService.showOnce();
            case "REPLACE_FILE" -> result = deviceService.replaceFile(request.get("new_file"));
            case "GET_CURRENT_FILE" -> result = deviceService.getCurrentFileInfo();
            case "RANDOMIZE_DEVICE" -> result = deviceService.randomizeDevice(parseInt(request.get("id"), -1));
            case "SERVICE_ALL" -> result = deviceService.serviceAll();
            case "SMART_MODE" -> result = deviceService.smartMode();
            default -> {
                return errorResponse("Неизвестная команда клиента: " + command);
            }
        }

        return toMessage(result);
    }

    private ProtocolTextMessage toMessage(OperationResult result) {
        ProtocolTextMessage response = new ProtocolTextMessage();
        response.put("status", result.isSuccess() ? "OK" : "ERROR");
        response.put("message", result.getMessage());
        response.put("current_file", result.getFileName());
        response.put("objects_found", String.valueOf(result.getObjectsFound()));
        response.put("properties_read", String.valueOf(result.getPropertiesRead()));
        response.put("properties_missing", String.valueOf(result.getPropertiesMissing()));

        if (!result.getActionMessages().isEmpty()) {
            response.put("action_message", String.join(" | ", result.getActionMessages()));
        }

        for (DeviceSnapshot snapshot : result.getSnapshots()) {
            response.addDeviceLine(snapshot.toCompactLine());
        }
        return response;
    }

    private ProtocolTextMessage errorResponse(String message) {
        ProtocolTextMessage response = new ProtocolTextMessage();
        response.put("status", "ERROR");
        response.put("message", message);
        return response;
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
