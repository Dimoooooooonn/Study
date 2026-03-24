package lab5.model;

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

    protected GardenDevice(){
        id = next_id++;
        device_manufacturer = "unknown";
        device_model = "unknown";
        power_supply = "unknown";
        powered_on = false;
        work_intensity = 0;
        wear_level = 0;
    }

    protected GardenDevice(String device_manufacturer, String device_model, String power_supply, boolean powered_on, short work_intensity, int wear_level){
        id = next_id++;
        setValue(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
    }

    protected void setValue(String device_manufacturer, String device_model, String power_supply, boolean powered_on, short work_intensity, int wear_level){
        this.device_manufacturer = device_manufacturer;
        this.device_model = device_model;
        this.power_supply = power_supply;
        this.powered_on = powered_on;
        this.work_intensity = work_intensity;
        this.wear_level = wear_level;
    }

    protected int clampValue(int value, int min, int max){
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }

    public int getId() { return id; }
    public String getDeviceManufacturer() { return device_manufacturer; }
    public String getDeviceModel() { return device_model; }
    public String getPowerSupply() { return power_supply; }
    public boolean getPoweredOn() { return powered_on; }
    public short getWorkIntensity() { return work_intensity; }
    public int getWearLevel() { return wear_level; }

    public void setDeviceManufacturer(String device_manufacturer) { this.device_manufacturer = device_manufacturer; }
    public void setDeviceModel(String device_model) { this.device_model = device_model; }
    public void setPowerSupply(String power_supply) { this.power_supply = power_supply; }
    public void setPoweredOn(boolean powered_on) { this.powered_on = powered_on; }
    public void setWorkIntensity(short work_intensity) { this.work_intensity = work_intensity; }
    public void setWearLevel(int wear_level) { this.wear_level = wear_level; }

    public String buildInfo(){
        return "\nId: " + id +
                "\nТип: " + getClass().getSimpleName() +
                "\nПроизводитель: " + device_manufacturer +
                "\nМодель: " + device_model +
                "\nИсточник питания: " + power_supply +
                "\nИнтенсивность работы: " + work_intensity +
                "\nИзнос: " + wear_level +
                "\n" + deviceState() +
                "\n" + specificInfo();
    }

    public String turnOnDevice(){
        if (powered_on) { return "Устройство уже включено"; }
        powered_on = true;
        return "Устройство включено";
    }

    public String turnOffDevice(){
        if (!powered_on) { return "Устройство уже выключено"; }
        powered_on = false;
        return "Устройство выключено";
    }

    public String deviceState(){
        if (powered_on) { return "Устройство " + id + " включено"; }
        return "Устройство " + id + " выключено";
    }

    public int randomValue(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public String serviceDevice() {
        int decrease = randomValue(10, 25);
        wear_level = clampValue(wear_level - decrease, 0, 100);
        return id + ": проведено обслуживание. Износ уменьшен на " + decrease;
    }

    public String getIdentityKey(){
        return getClass().getSimpleName() + "|" + device_manufacturer + "|" + device_model + "|" + power_supply;
    }

    public Map<String, String> getProperties(){
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

    protected abstract void fillSpecificProperties(Map<String, String> properties);
    public abstract String changeWorkIntensivity(short intensivity);
    public abstract String deviceOperation();
    public abstract void randomizeSpecificProperties();
    protected abstract String specificInfo();
}
