package lab6.server.service;

import lab6.common.dto.DeviceSnapshot;
import lab6.server.model.GardenDevice;

import java.io.IOException;
import java.util.List;

public class DeviceService {
    private final DeviceFileStorage fileStorage;
    private String currentFileName;

    public DeviceService(String currentFileName) {
        this.fileStorage = new DeviceFileStorage();
        this.currentFileName = currentFileName;
    }

    public synchronized OperationResult showOnce() {
        try {
            ReadResult readResult = fileStorage.readDevices(currentFileName);
            currentFileName = readResult.getFileName();
            return fromReadResult(readResult);
        } catch (IOException e) {
            return errorResult(e.getMessage());
        }
    }

    public synchronized OperationResult replaceFile(String newFileName) {
        String oldFileName = currentFileName;
        currentFileName = newFileName;
        OperationResult result = showOnce();
        if (!result.isSuccess()) {
            currentFileName = oldFileName;
            result.setMessage("Ошибка замены файла: " + result.getMessage());
            result.setFileName(oldFileName);
        } else {
            result.setMessage("Сервер переключен на новый файл: " + newFileName);
        }
        return result;
    }

    public synchronized OperationResult getCurrentFileInfo() {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        result.setMessage("Текущее имя файла на сервере");
        result.setFileName(currentFileName);
        return result;
    }

    public synchronized OperationResult randomizeDevice(int id) {
        return updateDevices(list -> {
            for (GardenDevice device : list) {
                if (device.getId() == id) {
                    device.randomizeSpecificProperties();
                    return "Свойства устройства с id=" + id + " случайно изменены на сервере";
                }
            }
            return "Устройство с id=" + id + " не найдено";
        });
    }

    public synchronized OperationResult serviceAll() {
        return updateDevices(list -> {
            StringBuilder builder = new StringBuilder();
            for (GardenDevice device : list) {
                builder.append(device.serviceDevice()).append('\n');
            }
            return builder.toString().trim();
        });
    }

    public synchronized OperationResult smartMode() {
        return updateDevices(list -> {
            StringBuilder builder = new StringBuilder();
            for (GardenDevice device : list) {
                if (!device.getPoweredOn()) {
                    builder.append(device.turnOnDevice()).append("\n");
                }
                if (device.getWorkIntensity() == 0) {
                    builder.append(device.changeWorkIntensivity((short) 1)).append("\n");
                }
                builder.append(device.deviceOperation()).append("\n");
            }
            return builder.toString().trim();
        });
    }

    private OperationResult updateDevices(DeviceUpdater updater) {
        try {
            ReadResult readResult = fileStorage.readDevices(currentFileName);
            currentFileName = readResult.getFileName();
            List<GardenDevice> devices = readResult.getDevices();
            String actionMessage = updater.update(devices);
            fileStorage.writeDevices(currentFileName, devices);

            OperationResult result = fromReadResult(new ReadResult(devices, devices.size(),
                    readResult.getPropertiesRead(), readResult.getPropertiesMissing(), currentFileName,
                    "Изменения сохранены на сервере"));
            result.setMessage(actionMessage);
            result.getActionMessages().add(actionMessage);
            return result;
        } catch (IOException e) {
            return errorResult(e.getMessage());
        }
    }

    private OperationResult fromReadResult(ReadResult readResult) {
        OperationResult result = new OperationResult();
        result.setSuccess(true);
        result.setMessage(readResult.getMessage());
        result.setFileName(currentFileName);
        result.setObjectsFound(readResult.getObjectsFound());
        result.setPropertiesRead(readResult.getPropertiesRead());
        result.setPropertiesMissing(readResult.getPropertiesMissing());
        for (GardenDevice device : readResult.getDevices()) {
            result.getSnapshots().add(device.toSnapshot());
        }
        return result;
    }

    private OperationResult errorResult(String message) {
        OperationResult result = new OperationResult();
        result.setSuccess(false);
        result.setMessage(message);
        result.setFileName(currentFileName);
        return result;
    }

    private interface DeviceUpdater {
        String update(List<GardenDevice> list);
    }
}
