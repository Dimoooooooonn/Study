package lab6.server.service;

import lab6.server.model.AutomaticFiring;
import lab6.server.model.GardenDevice;
import lab6.server.model.LavnMover;
import lab6.server.model.ThermalDrive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeviceFileStorage {
    public ReadResult readDevices(String fileName) throws IOException {
        File file = findFile(fileName);
        List<GardenDevice> devices = new ArrayList<>();
        int propertiesRead = 0;
        int propertiesMissing = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> block = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!block.isEmpty()) {
                        ParseResult parseResult = parseBlock(block);
                        if (parseResult.device != null) {
                            devices.add(parseResult.device);
                            propertiesRead += parseResult.propertiesRead;
                            propertiesMissing += parseResult.propertiesMissing;
                        }
                        block.clear();
                    }
                } else {
                    block.add(line);
                }
            }

            if (!block.isEmpty()) {
                ParseResult parseResult = parseBlock(block);
                if (parseResult.device != null) {
                    devices.add(parseResult.device);
                    propertiesRead += parseResult.propertiesRead;
                    propertiesMissing += parseResult.propertiesMissing;
                }
            }
        }

        return new ReadResult(devices, devices.size(), propertiesRead, propertiesMissing,
                file.getPath(), "Файл успешно прочитан на сервере");
    }

    public void writeDevices(String fileName, List<GardenDevice> devices) throws IOException {
        File file = findFileForWrite(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (GardenDevice device : devices) {
                Map<String, String> properties = device.getProperties();
                writer.write("Type: " + device.getClass().getSimpleName() + ",");
                writer.newLine();
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    writer.write(entry.getKey() + ": " + entry.getValue() + ",");
                    writer.newLine();
                }
                writer.newLine();
            }
        }
    }

    private ParseResult parseBlock(List<String> block) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        for (String line : block) {
            int index = line.indexOf(':');
            if (index <= 0) {
                continue;
            }
            String key = line.substring(0, index).trim();
            String value = line.substring(index + 1).trim();
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1).trim();
            }
            values.put(key, value);
        }

        String type = values.getOrDefault("Type", "");
        if (type.isBlank()) {
            return new ParseResult(null, 0, 0);
        }

        return switch (type) {
            case "LavnMover" -> buildLawnMover(values);
            case "AutomaticFiring" -> buildAutomaticFiring(values);
            case "ThermalDrive" -> buildThermalDrive(values);
            default -> new ParseResult(null, 0, 0);
        };
    }

    private ParseResult buildLawnMover(Map<String, String> values) {
        String[] required = {"device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                "grass_container_level", "blade_sharpness", "cut_height"};
        Counter counter = count(values, required);

        LavnMover device = new LavnMover(
                getString(values, "device_manufacturer", "unknown"),
                getString(values, "device_model", "unknown"),
                getString(values, "power_supply", "unknown"),
                getBoolean(values, "powered_on", false),
                (short) getInt(values, "work_intensity", 0),
                getInt(values, "wear_level", 0),
                getInt(values, "grass_container_level", 0),
                getInt(values, "blade_sharpness", 0),
                getInt(values, "cut_height", 2)
        );
        return new ParseResult(device, counter.read, counter.missing);
    }

    private ParseResult buildAutomaticFiring(Map<String, String> values) {
        String[] required = {"device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                "water_tank_level", "watering_duration", "soil_moisture", "active_zone"};
        Counter counter = count(values, required);

        AutomaticFiring device = new AutomaticFiring(
                getString(values, "device_manufacturer", "unknown"),
                getString(values, "device_model", "unknown"),
                getString(values, "power_supply", "unknown"),
                getBoolean(values, "powered_on", false),
                (short) getInt(values, "work_intensity", 0),
                getInt(values, "wear_level", 0),
                getInt(values, "water_tank_level", 0),
                getInt(values, "watering_duration", 1),
                getInt(values, "soil_moisture", 0),
                getInt(values, "active_zone", 1)
        );
        return new ParseResult(device, counter.read, counter.missing);
    }

    private ParseResult buildThermalDrive(Map<String, String> values) {
        String[] required = {"device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                "greenhouse_temperature", "window_opening_percent", "target_temperature", "response_speed"};
        Counter counter = count(values, required);

        ThermalDrive device = new ThermalDrive(
                getString(values, "device_manufacturer", "unknown"),
                getString(values, "device_model", "unknown"),
                getString(values, "power_supply", "unknown"),
                getBoolean(values, "powered_on", false),
                (short) getInt(values, "work_intensity", 0),
                getInt(values, "wear_level", 0),
                getInt(values, "greenhouse_temperature", 0),
                getInt(values, "window_opening_percent", 0),
                getInt(values, "target_temperature", 20),
                getInt(values, "response_speed", 1)
        );
        return new ParseResult(device, counter.read, counter.missing);
    }

    private Counter count(Map<String, String> values, String[] required) {
        Counter counter = new Counter();
        for (String key : required) {
            if (values.containsKey(key)) {
                counter.read++;
            } else {
                counter.missing++;
            }
        }
        return counter;
    }

    private String getString(Map<String, String> values, String key, String defaultValue) {
        return values.getOrDefault(key, defaultValue);
    }

    private boolean getBoolean(Map<String, String> values, String key, boolean defaultValue) {
        if (!values.containsKey(key)) {
            return defaultValue;
        }
        return Boolean.parseBoolean(values.get(key));
    }

    private int getInt(Map<String, String> values, String key, int defaultValue) {
        if (!values.containsKey(key)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(values.get(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private File findFile(String fileName) throws IOException {
        File direct = new File(fileName);
        if (direct.exists()) {
            return direct;
        }

        File serverData = new File("server_data" + File.separator + fileName);
        if (serverData.exists()) {
            return serverData;
        }

        File projectData = new File("garden-server" + File.separator + "server_data" + File.separator + fileName);
        if (projectData.exists()) {
            return projectData;
        }

        throw new IOException("Не удалось найти файл на сервере: " + fileName);
    }

    private File findFileForWrite(String fileName) throws IOException {
        File direct = new File(fileName);
        if (direct.exists()) {
            return direct;
        }

        File serverData = new File("server_data" + File.separator + fileName);
        if (serverData.exists()) {
            return serverData;
        }

        File projectData = new File("garden-server" + File.separator + "server_data" + File.separator + fileName);
        if (projectData.exists()) {
            return projectData;
        }

        File parent = direct.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (direct.createNewFile()) {
            return direct;
        }
        throw new IOException("Не удалось подготовить файл для записи: " + fileName);
    }

    private static class ParseResult {
        private final GardenDevice device;
        private final int propertiesRead;
        private final int propertiesMissing;

        private ParseResult(GardenDevice device, int propertiesRead, int propertiesMissing) {
            this.device = device;
            this.propertiesRead = propertiesRead;
            this.propertiesMissing = propertiesMissing;
        }
    }

    private static class Counter {
        private int read;
        private int missing;
    }
}
