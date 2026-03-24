package lab5.model;

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
    private final String csv_file_name;

    public DeviceCsvLogger(String csv_file_name){
        this.csv_file_name = csv_file_name;
    }

    public String getCsvFileName() {
        return csv_file_name;
    }

    public int logChanges(List<GardenDevice> old_devices, List<GardenDevice> new_devices){
        Map<String, GardenDevice> old_map = toMap(old_devices);
        Map<String, GardenDevice> new_map = toMap(new_devices);

        int changes_count = 0;
        boolean need_header = !new File(csv_file_name).exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csv_file_name, true))) {
            if (need_header) {
                writer.write("timestamp,device_type,device_key,property_name,old_value,new_value");
                writer.newLine();
            }

            for (Map.Entry<String, GardenDevice> entry: new_map.entrySet()) {
                String key = entry.getKey();
                GardenDevice new_device = entry.getValue();
                GardenDevice old_device = old_map.get(key);

                if (old_device == null) {
                    Map<String, String> new_properties = new_device.getProperties();
                    for (Map.Entry<String, String> property: new_properties.entrySet()) {
                        writeRow(writer, new_device, key, property.getKey(), "", property.getValue());
                        changes_count++;
                    }
                }
                else {
                    Map<String, String> old_properties = old_device.getProperties();
                    Map<String, String> new_properties = new_device.getProperties();

                    for (Map.Entry<String, String> property: new_properties.entrySet()) {
                        String property_name = property.getKey();
                        String old_value = old_properties.get(property_name);
                        String new_value = property.getValue();

                        if (old_value == null) { old_value = ""; }
                        if (!old_value.equals(new_value)) {
                            writeRow(writer, new_device, key, property_name, old_value, new_value);
                            changes_count++;
                        }
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("Ошибка при записи CSV: " + e.getMessage());
        }

        return changes_count;
    }

    private Map<String, GardenDevice> toMap(List<GardenDevice> devices){
        Map<String, GardenDevice> map = new LinkedHashMap<>();
        for (GardenDevice device: devices) {
            map.put(device.getIdentityKey(), device);
        }
        return map;
    }

    private void writeRow(BufferedWriter writer, GardenDevice device, String key, String property_name, String old_value, String new_value) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.write(escape(timestamp) + "," +
                escape(device.getClass().getSimpleName()) + "," +
                escape(key) + "," +
                escape(property_name) + "," +
                escape(old_value) + "," +
                escape(new_value));
        writer.newLine();
    }

    private String escape(String value){
        if (value == null) { return "\"\""; }
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
