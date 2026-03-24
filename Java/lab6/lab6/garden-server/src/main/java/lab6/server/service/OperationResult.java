package lab6.server.service;

import lab6.common.dto.DeviceSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OperationResult {
    private boolean success;
    private String message;
    private String fileName;
    private int objectsFound;
    private int propertiesRead;
    private int propertiesMissing;
    private final List<DeviceSnapshot> snapshots = new ArrayList<>();
    private final List<String> actionMessages = new ArrayList<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getObjectsFound() {
        return objectsFound;
    }

    public void setObjectsFound(int objectsFound) {
        this.objectsFound = objectsFound;
    }

    public int getPropertiesRead() {
        return propertiesRead;
    }

    public void setPropertiesRead(int propertiesRead) {
        this.propertiesRead = propertiesRead;
    }

    public int getPropertiesMissing() {
        return propertiesMissing;
    }

    public void setPropertiesMissing(int propertiesMissing) {
        this.propertiesMissing = propertiesMissing;
    }

    public List<DeviceSnapshot> getSnapshots() {
        return snapshots;
    }

    public List<String> getActionMessages() {
        return actionMessages;
    }
}
