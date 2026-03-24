import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class DeviceFileReader {
    private String file_name;
    private int found_objects;
    private int found_properties;
    private int missing_properties;

    public DeviceFileReader(String file_name){
        this.file_name = file_name;
        found_objects = 0;
        found_properties = 0;
        missing_properties = 0;
    }

    public void setFileName(String file_name) { this.file_name = file_name; }
    public String getFileName() { return file_name; }
    public int getFoundObjects() { return found_objects; }
    public int getFoundProperties() { return found_properties; }
    public int getMissingProperties() { return missing_properties; }

    public LinkedList<GardenDevice> readDevices(){
        LinkedList<GardenDevice> read_devices = new LinkedList<>();
        found_objects = 0;
        found_properties = 0;
        missing_properties = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file_name))) {
            String line;
            StringBuilder block = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    parseBlock(block.toString(), read_devices);
                    block.setLength(0);
                }
                else {
                    block.append(line).append("\n");
                }
            }
            parseBlock(block.toString(), read_devices);
        }
        catch (IOException e){
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return read_devices;
    }

    private void parseBlock(String block, LinkedList<GardenDevice> read_devices){
        if (block == null || block.trim().isEmpty()) { return; }

        String type = findField(block, "Type");
        if (type == null) { return; }

        String normalized_type = type.trim().toLowerCase();

        if (normalized_type.equals("lavnmover") || normalized_type.equals("газонокосилка")) {
            String[] fields = {
                    "device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                    "grass_container_level", "blade_sharpness", "cut_height"
            };
            countFields(block, fields);

            LavnMover device = new LavnMover(
                    getStringField(block, "device_manufacturer", "unknown"),
                    getStringField(block, "device_model", "unknown"),
                    getStringField(block, "power_supply", "unknown"),
                    getBooleanField(block, "powered_on", false),
                    getShortField(block, "work_intensity", (short)0),
                    getIntField(block, "wear_level", 0),
                    getIntField(block, "grass_container_level", 0),
                    getIntField(block, "blade_sharpness", 0),
                    getIntField(block, "cut_height", 0)
            );
            read_devices.add(device);
            found_objects++;
        }
        else if (normalized_type.equals("automaticfiring") || normalized_type.equals("автополив") || normalized_type.equals("автоматическийполив")) {
            String[] fields = {
                    "device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                    "water_tank_level", "watering_duration", "soil_moisture", "active_zone"
            };
            countFields(block, fields);

            AutomaticFiring device = new AutomaticFiring(
                    getStringField(block, "device_manufacturer", "unknown"),
                    getStringField(block, "device_model", "unknown"),
                    getStringField(block, "power_supply", "unknown"),
                    getBooleanField(block, "powered_on", false),
                    getShortField(block, "work_intensity", (short)0),
                    getIntField(block, "wear_level", 0),
                    getIntField(block, "water_tank_level", 0),
                    getIntField(block, "watering_duration", 0),
                    getIntField(block, "soil_moisture", 0),
                    getIntField(block, "active_zone", 0)
            );
            read_devices.add(device);
            found_objects++;
        }
        else if (normalized_type.equals("thermaldrive") || normalized_type.equals("термопривод")) {
            String[] fields = {
                    "device_manufacturer", "device_model", "power_supply", "powered_on", "work_intensity", "wear_level",
                    "greenhouse_temperature", "window_opening_percent", "target_temperature", "response_speed"
            };
            countFields(block, fields);

            ThermalDrive device = new ThermalDrive(
                    getStringField(block, "device_manufacturer", "unknown"),
                    getStringField(block, "device_model", "unknown"),
                    getStringField(block, "power_supply", "unknown"),
                    getBooleanField(block, "powered_on", false),
                    getShortField(block, "work_intensity", (short)0),
                    getIntField(block, "wear_level", 0),
                    getIntField(block, "greenhouse_temperature", 0),
                    getIntField(block, "window_opening_percent", 0),
                    getIntField(block, "target_temperature", 0),
                    getIntField(block, "response_speed", 0)
            );
            read_devices.add(device);
            found_objects++;
        }
        else {
            System.out.println("Неизвестный тип устройства в файле: " + type);
        }
    }

    private void countFields(String block, String[] fields){
        for (String field: fields){
            if (findField(block, field) == null) { missing_properties++; }
            else { found_properties++; }
        }
    }

    private String findField(String block, String field_name){
        String key = field_name + ":";
        int start_index = block.indexOf(key);
        if (start_index == -1) { return null; }

        int value_start = start_index + key.length();
        while (value_start < block.length() && Character.isWhitespace(block.charAt(value_start))) {
            value_start++;
        }

        int value_end = value_start;
        while (value_end < block.length()
                && block.charAt(value_end) != ','
                && block.charAt(value_end) != '\n'
                && block.charAt(value_end) != ';') {
            value_end++;
        }

        return block.substring(value_start, value_end).trim();
    }

    private String getStringField(String block, String field_name, String default_value){
        String value = findField(block, field_name);
        if (value == null || value.isEmpty()) { return default_value; }
        return value;
    }

    private int getIntField(String block, String field_name, int default_value){
        String value = findField(block, field_name);
        if (value == null) { return default_value; }
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            return default_value;
        }
    }

    private short getShortField(String block, String field_name, short default_value){
        String value = findField(block, field_name);
        if (value == null) { return default_value; }
        try {
            return Short.parseShort(value);
        }
        catch (NumberFormatException e){
            return default_value;
        }
    }

    private boolean getBooleanField(String block, String field_name, boolean default_value){
        String value = findField(block, field_name);
        if (value == null) { return default_value; }

        String normalized_value = value.toLowerCase();
        if (normalized_value.equals("true") || normalized_value.equals("1") || normalized_value.equals("да") || normalized_value.equals("включено")) {
            return true;
        }
        if (normalized_value.equals("false") || normalized_value.equals("0") || normalized_value.equals("нет") || normalized_value.equals("выключено")) {
            return false;
        }
        return default_value;
    }
}
