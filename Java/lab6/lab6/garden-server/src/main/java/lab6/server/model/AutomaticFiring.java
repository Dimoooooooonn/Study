package lab6.server.model;

import java.util.Map;

public class AutomaticFiring extends GardenDevice {
    private int water_tank_level;
    private int watering_duration;
    private int soil_moisture;
    private int active_zone;
    private int liters_per_minute;

    public AutomaticFiring() {
        super();
        water_tank_level = 0;
        watering_duration = 0;
        soil_moisture = 0;
        active_zone = 1;
        liters_per_minute = 0;
    }

    public AutomaticFiring(String device_manufacturer, String device_model, String power_supply, boolean powered_on,
                           short work_intensity, int wear_level, int water_tank_level,
                           int watering_duration, int soil_moisture, int active_zone) {
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.water_tank_level = water_tank_level;
        this.watering_duration = watering_duration;
        this.soil_moisture = soil_moisture;
        this.active_zone = active_zone;
        liters_per_minute = 0;
    }

    @Override
    public String changeWorkIntensivity(short intensivity) {
        if (intensivity >= 1 && intensivity <= 4) {
            work_intensity = intensivity;
            switch (intensivity) {
                case 1 -> liters_per_minute = 3;
                case 2 -> liters_per_minute = 5;
                case 3 -> liters_per_minute = 7;
                case 4 -> liters_per_minute = 10;
                default -> liters_per_minute = 0;
            }
            return "Интенсивность полива: " + liters_per_minute + " л/мин";
        }
        return "Некорректная интенсивность";
    }

    @Override
    public String deviceOperation() {
        if (!powered_on) {
            return "Автополив выключен. Сначала включите устройство";
        }
        if (water_tank_level < 15) {
            return "Недостаточно воды в баке. Заполните бак";
        }
        if (wear_level >= 90) {
            return "Система полива слишком изношена. Нужен ремонт";
        }

        int waterUsed = randomValue(10, 20);
        int moistureIncrease = randomValue(12, 25);
        int wearIncrease = randomValue(2, 6);

        water_tank_level = clampValue(water_tank_level - waterUsed, 0, 100);
        soil_moisture = clampValue(soil_moisture + moistureIncrease, 0, 100);
        wear_level = clampValue(wear_level + wearIncrease, 0, 100);
        return "Выполняется полив в зоне " + active_zone + " в течение " + watering_duration + " минут";
    }

    public String refillTank() {
        water_tank_level = 100;
        return "Бак полностью заполнен водой";
    }

    public String switchZone(int zone) {
        active_zone = clampValue(zone, 1, 10);
        return "Полив переключен на зону " + active_zone;
    }

    @Override
    public void randomizeSpecificProperties() {
        water_tank_level = clampValue(water_tank_level + randomValue(-20, 10), 0, 100);
        watering_duration = clampValue(watering_duration + randomValue(-10, 10), 1, 120);
        soil_moisture = clampValue(soil_moisture + randomValue(-15, 15), 0, 100);
        active_zone = clampValue(active_zone + randomValue(-1, 1), 1, 10);
    }

    @Override
    protected void fillSpecificProperties(Map<String, String> properties) {
        properties.put("water_tank_level", String.valueOf(water_tank_level));
        properties.put("watering_duration", String.valueOf(watering_duration));
        properties.put("soil_moisture", String.valueOf(soil_moisture));
        properties.put("active_zone", String.valueOf(active_zone));
        properties.put("liters_per_minute", String.valueOf(liters_per_minute));
    }
}
