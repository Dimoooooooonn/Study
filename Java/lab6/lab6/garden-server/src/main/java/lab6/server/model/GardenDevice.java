package lab6.server.model;

import lab6.common.dto.DeviceSnapshot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public abstract class GardenDevice {
    private static int next_id = 1;

    protected final int id;
    protected String device_manufacturer;
    protected String device_model;
    protected String power_supply;
    protected boolean powered_on;
    protected short work_intensity;
    protected int wear_level;

    protected GardenDevice() {
        id = next_id++;
        device_manufacturer = "unknown";
        device_model = "unknown";
        power_supply = "unknown";
        powered_on = false;
        work_intensity = 0;
        wear_level = 0;
    }

    protected GardenDevice(String device_manufacturer, String device_model, String power_supply, boolean powered_on,
                           short work_intensity, int wear_level) {
        id = next_id++;
        setValue(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
    }

    protected void setValue(String device_manufacturer, String device_model, String power_supply, boolean powered_on,
                            short work_intensity, int wear_level) {
        this.device_manufacturer = device_manufacturer;
        this.device_model = device_model;
        this.power_supply = power_supply;
        this.powered_on = powered_on;
        this.work_intensity = work_intensity;
        this.wear_level = wear_level;
    }

    protected int clampValue(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public int getId() {
        return id;
    }

    public String getDeviceManufacturer() {
        return device_manufacturer;
    }

    public String getDeviceModel() {
        return device_model;
    }

    public String getPowerSupply() {
        return power_supply;
    }

    public boolean getPoweredOn() {
        return powered_on;
    }

    public short getWorkIntensity() {
        return work_intensity;
    }

    public int getWearLevel() {
        return wear_level;
    }

    public String turnOnDevice() {
        if (powered_on) {
            return "Устройство уже включено";
        }
        powered_on = true;
        return "Устройство включено";
    }

    public String turnOffDevice() {
        if (!powered_on) {
            return "Устройство уже выключено";
        }
        powered_on = false;
        return "Устройство выключено";
    }

    public int randomValue(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public String serviceDevice() {
        int decrease = randomValue(10, 25);
        wear_level = clampValue(wear_level - decrease, 0, 100);
        return id + ": проведено обслуживание. Износ уменьшен на " + decrease;
    }

    public String getIdentityKey() {
        return getClass().getSimpleName() + "|" + device_manufacturer + "|" + device_model + "|" + power_supply;
    }

    public Map<String, String> getProperties() {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        properties.put("device_manufacturer", device_manufacturer);
        properties.put("device_model", device_model);
        properties.put("power_supply", power_supply);
        properties.put("powered_on", String.valueOf(powered_on));
        properties.put("work_intensity", String.valueOf(work_intensity));
        properties.put("wear_level", String.valueOf(wear_level));
        fillSpecificProperties(properties);
        return properties;
    }

    public DeviceSnapshot toSnapshot() {
        DeviceSnapshot snapshot = new DeviceSnapshot();
        snapshot.setType(getClass().getSimpleName());
        snapshot.setId(id);
        snapshot.setManufacturer(device_manufacturer);
        snapshot.setModel(device_model);
        snapshot.setPowerSupply(power_supply);
        snapshot.setPoweredOn(powered_on);
        snapshot.setWorkIntensity(work_intensity);
        snapshot.setWearLevel(wear_level);
        snapshot.getExtraProperties().putAll(getSpecificProperties());
        return snapshot;
    }

    private Map<String, String> getSpecificProperties() {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        fillSpecificProperties(properties);
        properties.remove("device_manufacturer");
        properties.remove("device_model");
        properties.remove("power_supply");
        properties.remove("powered_on");
        properties.remove("work_intensity");
        properties.remove("wear_level");
        return properties;
    }

    public String buildInfo() {
        return toSnapshot().prettyString();
    }

    protected abstract void fillSpecificProperties(Map<String, String> properties);

    public abstract String changeWorkIntensivity(short intensivity);

    public abstract String deviceOperation();

    public abstract void randomizeSpecificProperties();
}
