package lab6.server.service;

import lab6.server.model.GardenDevice;

import java.util.List;

public class ReadResult {
    private final List<GardenDevice> devices;
    private final int objectsFound;
    private final int propertiesRead;
    private final int propertiesMissing;
    private final String fileName;
    private final String message;

    public ReadResult(List<GardenDevice> devices, int objectsFound, int propertiesRead, int propertiesMissing,
                      String fileName, String message) {
        this.devices = devices;
        this.objectsFound = objectsFound;
        this.propertiesRead = propertiesRead;
        this.propertiesMissing = propertiesMissing;
        this.fileName = fileName;
        this.message = message;
    }

    public List<GardenDevice> getDevices() {
        return devices;
    }

    public int getObjectsFound() {
        return objectsFound;
    }

    public int getPropertiesRead() {
        return propertiesRead;
    }

    public int getPropertiesMissing() {
        return propertiesMissing;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }
}
