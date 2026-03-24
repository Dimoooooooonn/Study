package lab6.client.util;

import lab6.common.dto.DeviceSnapshot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeviceCsvLogger {
    private final File csvFile;

    public DeviceCsvLogger(String fileName) {
        this.csvFile = new File(fileName);
    }

    public void appendChanges(List<DeviceSnapshot> oldDevices, List<DeviceSnapshot> newDevices) throws IOException {
        boolean writeHeader = !csvFile.exists() || csvFile.length() == 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            if (writeHeader) {
                writer.write("timestamp,device_type,device_key,property_name,old_value,new_value");
                writer.newLine();
            }

            Map<String, DeviceSnapshot> oldMap = toMap(oldDevices);
            Map<String, DeviceSnapshot> newMap = toMap(newDevices);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            for (Map.Entry<String, DeviceSnapshot> entry : newMap.entrySet()) {
                DeviceSnapshot oldDevice = oldMap.get(entry.getKey());
                DeviceSnapshot newDevice = entry.getValue();
                if (oldDevice == null) {
                    for (Map.Entry<String, String> propertyEntry : newDevice.getAllProperties().entrySet()) {
                        writeLine(writer, timestamp, newDevice.getType(), newDevice.getIdentityKey(), propertyEntry.getKey(), "", propertyEntry.getValue());
                    }
                    continue;
                }

                Map<String, String> oldProperties = oldDevice.getAllProperties();
                Map<String, String> newProperties = newDevice.getAllProperties();
                for (Map.Entry<String, String> propertyEntry : newProperties.entrySet()) {
                    String oldValue = oldProperties.get(propertyEntry.getKey());
                    String newValue = propertyEntry.getValue();
                    if (oldValue == null || !oldValue.equals(newValue)) {
                        writeLine(writer, timestamp, newDevice.getType(), newDevice.getIdentityKey(), propertyEntry.getKey(), oldValue, newValue);
                    }
                }
            }
        }
    }

    private Map<String, DeviceSnapshot> toMap(List<DeviceSnapshot> devices) {
        LinkedHashMap<String, DeviceSnapshot> map = new LinkedHashMap<>();
        if (devices == null) {
            return map;
        }
        for (DeviceSnapshot device : devices) {
            map.put(device.getIdentityKey(), device);
        }
        return map;
    }

    private void writeLine(BufferedWriter writer, String timestamp, String type, String key, String property,
                           String oldValue, String newValue) throws IOException {
        writer.write(csvEscape(timestamp) + "," + csvEscape(type) + "," + csvEscape(key) + "," +
                csvEscape(property) + "," + csvEscape(oldValue) + "," + csvEscape(newValue));
        writer.newLine();
    }

    private String csvEscape(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
