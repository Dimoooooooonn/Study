import java.util.Random;
import static java.lang.Math.clamp;


abstract class GardenDevice {
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
    protected void setWorkIntensity(short work_intensity) { this.work_intensity = work_intensity; }

    public String getDeviceManufacture() { return device_manufacturer; }
    public String getDeviceModel() { return device_model; }
    public String getPowerSupply() { return power_supply; }
    public boolean getPowered_on() { return powered_on; }
    public short getWorkIntensity() { return work_intensity; }
    public int getWearLevel() {return wear_level; }


    protected void printInfo(){
        System.out.println("\nId: " + id);
        System.out.println("Производитель: " + device_manufacturer);
        System.out.println("Модель: " + device_model);
        System.out.println("Источник питания: " + power_supply);
        deviceState();
        System.out.println(specificInfo());
    }

    public void turnOnDevice(){
        if (powered_on) { return; }
        powered_on = true;
    }

    public void turnOffDevice(){
        if (powered_on) { powered_on = false; }
    }

    public void deviceState(){
        if (powered_on) { System.out.println("Устройство " + id + " включено");}
        else { System.out.println("Устройство " + id + " выключено");}
    }

    public int randomValue(int min, int max){
        Random random = new Random();
        return random.nextInt(min, max + 1);
    }

    public void serviceDevice() {
        int decrease = randomValue(10, 25);
        wear_level = clamp(wear_level - decrease, 0, 100);
        System.out.println(id + ": проведено обслуживание. Износ уменьшен на " + decrease);
    }

    public abstract void changeWorkIntensivity(short intrnsivity);
    public abstract void deviceOperation();
    public abstract void randomizeSpecificProperties();
    protected abstract String specificInfo();

}
