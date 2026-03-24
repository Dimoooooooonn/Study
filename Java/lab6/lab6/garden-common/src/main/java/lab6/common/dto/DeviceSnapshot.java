package lab6.common.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class DeviceSnapshot {
    private String type;
    private int id;
    private String manufacturer;
    private String model;
    private String powerSupply;
    private boolean poweredOn;
    private short workIntensity;
    private int wearLevel;
    private final LinkedHashMap<String, String> extraProperties = new LinkedHashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(String powerSupply) {
        this.powerSupply = powerSupply;
    }

    public boolean isPoweredOn() {
        return poweredOn;
    }

    public void setPoweredOn(boolean poweredOn) {
        this.poweredOn = poweredOn;
    }

    public short getWorkIntensity() {
        return workIntensity;
    }

    public void setWorkIntensity(short workIntensity) {
        this.workIntensity = workIntensity;
    }

    public int getWearLevel() {
        return wearLevel;
    }

    public void setWearLevel(int wearLevel) {
        this.wearLevel = wearLevel;
    }

    public Map<String, String> getExtraProperties() {
        return extraProperties;
    }

    public String getIdentityKey() {
        return type + "|" + manufacturer + "|" + model + "|" + powerSupply;
    }

    public LinkedHashMap<String, String> getAllProperties() {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        properties.put("device_manufacturer", manufacturer);
        properties.put("device_model", model);
        properties.put("power_supply", powerSupply);
        properties.put("powered_on", String.valueOf(poweredOn));
        properties.put("work_intensity", String.valueOf(workIntensity));
        properties.put("wear_level", String.valueOf(wearLevel));
        properties.putAll(extraProperties);
        return properties;
    }

    public String toCompactLine() {
        StringJoiner extras = new StringJoiner(";");
        for (Map.Entry<String, String> entry : extraProperties.entrySet()) {
            extras.add(entry.getKey() + "=" + entry.getValue());
        }
        return escape(type) + "|" + id + "|" + escape(manufacturer) + "|" + escape(model) + "|" +
                escape(powerSupply) + "|" + poweredOn + "|" + workIntensity + "|" + wearLevel + "|" +
                escape(extras.toString());
    }

    public static DeviceSnapshot fromCompactLine(String line) {
        String[] parts = line.split("\\|", 9);
        DeviceSnapshot snapshot = new DeviceSnapshot();
        snapshot.setType(unescape(getPart(parts, 0)));
        snapshot.setId(parseInt(getPart(parts, 1), 0));
        snapshot.setManufacturer(unescape(getPart(parts, 2)));
        snapshot.setModel(unescape(getPart(parts, 3)));
        snapshot.setPowerSupply(unescape(getPart(parts, 4)));
        snapshot.setPoweredOn(Boolean.parseBoolean(getPart(parts, 5)));
        snapshot.setWorkIntensity((short) parseInt(getPart(parts, 6), 0));
        snapshot.setWearLevel(parseInt(getPart(parts, 7), 0));

        String extraString = unescape(getPart(parts, 8));
        if (!extraString.isBlank()) {
            String[] extraPairs = extraString.split(";");
            for (String extraPair : extraPairs) {
                int index = extraPair.indexOf('=');
                if (index > 0) {
                    snapshot.getExtraProperties().put(extraPair.substring(0, index), extraPair.substring(index + 1));
                }
            }
        }
        return snapshot;
    }

    public String prettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\nId: ").append(id)
                .append("\nТип: ").append(type)
                .append("\nПроизводитель: ").append(manufacturer)
                .append("\nМодель: ").append(model)
                .append("\nИсточник питания: ").append(powerSupply)
                .append("\nИнтенсивность работы: ").append(workIntensity)
                .append("\nИзнос: ").append(wearLevel)
                .append("\nСостояние: ").append(poweredOn ? "включено" : "выключено");

        for (Map.Entry<String, String> entry : extraProperties.entrySet()) {
            builder.append("\n").append(translate(entry.getKey())).append(": ").append(entry.getValue());
        }
        builder.append('\n');
        return builder.toString();
    }

    private static String translate(String name) {
        return switch (name) {
            case "grass_container_level" -> "Заполненность травосборника";
            case "blade_sharpness" -> "Острота ножей";
            case "cut_height" -> "Высота скашивания";
            case "velocity" -> "Скорость";
            case "water_tank_level" -> "Уровень воды в баке";
            case "watering_duration" -> "Длительность полива";
            case "soil_moisture" -> "Влажность почвы";
            case "active_zone" -> "Активная зона полива";
            case "liters_per_minute" -> "Литров в минуту";
            case "greenhouse_temperature" -> "Температура в теплице";
            case "window_opening_percent" -> "Открытие окна";
            case "target_temperature" -> "Целевая температура";
            case "response_speed" -> "Скорость реакции";
            case "opening_time" -> "Время открытия";
            default -> name;
        };
    }

    private static String getPart(String[] parts, int index) {
        if (index < parts.length) {
            return parts[index];
        }
        return "";
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\p").replace(";", "\\s");
    }

    private static String unescape(String value) {
        StringBuilder builder = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (escaping) {
                if (current == 'p') {
                    builder.append('|');
                } else if (current == 's') {
                    builder.append(';');
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
