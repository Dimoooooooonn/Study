public class AutomaticFiring extends GardenDevice{
    private int water_tank_level;
    private int watering_duration;
    private int soil_moisture;
    private int active_zone;

    public AutomaticFiring(){
        super();
        water_tank_level = 0;
        watering_duration = 0;
        soil_moisture = 0;
        active_zone = 0;
    }

    public AutomaticFiring(String device_manufacturer, String device_model, String power_supply, boolean powered_on, short work_intensity, int wear_level,
                           int water_tank_level, int watering_duration, int soil_moisture, int active_zone){
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.water_tank_level = water_tank_level;
        this.watering_duration = watering_duration;
        this.soil_moisture = soil_moisture;
        this.active_zone = active_zone;
    }

    public void setWaterTankLevel(int water_tank_level) { this.water_tank_level = water_tank_level; }
    public void setWateringDuration(int watering_duration) { this.watering_duration = watering_duration; }
    public void setSoilMoisture(int soil_moisture) { this.soil_moisture = soil_moisture; }
    public void setActiveZone(int active_zone) { this.active_zone = active_zone; }

    @Override
    public void changeWorkIntensivity(short intensivity){
        if (!powered_on) {
            System.out.println("Прежде чем поменять длительность поливки включите автополив");
            work_intensity = intensivity;
            return;
        }
        if (intensivity == 1 || intensivity == 2 || intensivity == 3 || intensivity == 4){
            work_intensity = intensivity;
            switch (intensivity){
                case 1:
                    watering_duration = 60;
                    System.out.println("Длительность полива составляет: " + watering_duration + " минут");
                    break;
                case 2:
                    watering_duration = 80;
                    System.out.println("Длительность полива составляет: " + watering_duration + " минут");
                    break;
                case 3:
                    watering_duration = 100;
                    System.out.println("Длительность полива составляет: " + watering_duration + " минут");
                    break;
                case 4:
                    watering_duration = 120;
                    System.out.println("Длительность полива составляет: " + watering_duration + " минут");
                    break;
                default:
                    watering_duration = 0;
                    System.out.println("Длительность полива составляет: " + watering_duration + " минут");
                    break;
            }
        }
    }

    @Override
    public void deviceOperation(){
        if (!powered_on) {
            System.out.println("Система полива выключена. Сначала включите устройство");
            return;
        }
        if (water_tank_level < 15){
            System.out.println("Недостаточно воды в баке. Заполните бак");
            return;
        }
        if (wear_level >= 90){
            System.out.println("Система полива слишком изношена. Нужен ремонт");
            return;
        }
        int water_used = randomValue(10, 20);
        int moisture_increase = randomValue(12, 25);
        int wear_increase = randomValue(2, 6);

        water_tank_level = clampValue(water_tank_level - water_used, 0, 100);
        soil_moisture = clampValue(soil_moisture + moisture_increase, 0, 100);
        wear_level = clampValue(wear_level + wear_increase, 0, 100);

        System.out.println("Выполняется полив в зоне " + active_zone + " в течение " + watering_duration + " минут");
    }

    public void refillTank(){
        water_tank_level = 100;
        System.out.println("Бак полностью заполнен водой");
    }

    public void switchZone(int zone){
        active_zone = clampValue(zone, 1, 10);
        System.out.println("Полив переключен на зону " + active_zone);
    }

    @Override
    public void randomizeSpecificProperties(){
        water_tank_level = clampValue(water_tank_level + randomValue(-20, 10), 0, 100);
        watering_duration = clampValue(watering_duration + randomValue(-10, 10), 1, 120);
        soil_moisture = clampValue(soil_moisture + randomValue(-15, 15), 0, 100);
        active_zone = clampValue(active_zone + randomValue(-1, 1), 1, 10);
    }

    @Override
    protected String specificInfo() {
        return "Уровень воды в баке: " + water_tank_level +
                "\nДлительность полива: " + watering_duration + " мин" +
                "\nВлажность почвы: " + soil_moisture +
                "\nАктивная зона: " + active_zone + "\n";
    }
}
