package lab6.server.model;

import java.util.Map;

public class ThermalDrive extends GardenDevice {
    private int greenhouse_temperature;
    private int window_opening_percent;
    private int target_temperature;
    private int response_speed;
    private int opening_time;

    public ThermalDrive() {
        super();
        greenhouse_temperature = 0;
        window_opening_percent = 0;
        target_temperature = 20;
        response_speed = 1;
        opening_time = 0;
    }

    public ThermalDrive(String device_manufacturer, String device_model, String power_supply, boolean powered_on,
                        short work_intensity, int wear_level, int greenhouse_temperature,
                        int window_opening_percent, int target_temperature, int response_speed) {
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.greenhouse_temperature = greenhouse_temperature;
        this.window_opening_percent = window_opening_percent;
        this.target_temperature = target_temperature;
        this.response_speed = response_speed;
        opening_time = 0;
    }

    @Override
    public String changeWorkIntensivity(short intensivity) {
        if (intensivity >= 1 && intensivity <= 4) {
            work_intensity = intensivity;
            switch (intensivity) {
                case 1 -> opening_time = 20;
                case 2 -> opening_time = 15;
                case 3 -> opening_time = 10;
                case 4 -> opening_time = 5;
                default -> opening_time = 20;
            }
            return "Время открытия окна: " + opening_time + " сек";
        }
        return "Некорректная интенсивность";
    }

    @Override
    public String deviceOperation() {
        if (!powered_on) {
            return "Термопривод выключен. Сначала включите устройство";
        }
        if (wear_level >= 90) {
            return "Термопривод слишком изношен. Нужен ремонт";
        }

        if (greenhouse_temperature > target_temperature) {
            int openDelta = response_speed * 8;
            int coolDelta = randomValue(2, 5);
            window_opening_percent = clampValue(window_opening_percent + openDelta, 0, 100);
            greenhouse_temperature = clampValue(greenhouse_temperature - coolDelta, 0, 60);
            wear_level = clampValue(wear_level + randomValue(1, 4), 0, 100);
            return "Термопривод открывает окно для охлаждения теплицы";
        }

        if (greenhouse_temperature < target_temperature - 2) {
            int closeDelta = response_speed * 8;
            int warmDelta = randomValue(1, 3);
            window_opening_percent = clampValue(window_opening_percent - closeDelta, 0, 100);
            greenhouse_temperature = clampValue(greenhouse_temperature + warmDelta, 0, 60);
            wear_level = clampValue(wear_level + randomValue(1, 3), 0, 100);
            return "Термопривод прикрывает окно для сохранения тепла";
        }

        return "Температура близка к целевой. Коррекция не требуется";
    }

    public String calibrateSensor() {
        greenhouse_temperature = clampValue(greenhouse_temperature + randomValue(-1, 1), 0, 60);
        return "Датчик температуры откалиброван";
    }

    public String emergencyOpenWindow() {
        window_opening_percent = 100;
        return "Окно аварийно открыто на 100%";
    }

    public String closeWindow() {
        window_opening_percent = 0;
        return "Окно полностью закрыто";
    }

    @Override
    public void randomizeSpecificProperties() {
        greenhouse_temperature = clampValue(greenhouse_temperature + randomValue(-4, 4), 0, 60);
        window_opening_percent = clampValue(window_opening_percent + randomValue(-20, 20), 0, 100);
        target_temperature = clampValue(target_temperature + randomValue(-2, 2), 10, 35);
        response_speed = clampValue(response_speed + randomValue(-1, 1), 1, 10);
    }

    @Override
    protected void fillSpecificProperties(Map<String, String> properties) {
        properties.put("greenhouse_temperature", String.valueOf(greenhouse_temperature));
        properties.put("window_opening_percent", String.valueOf(window_opening_percent));
        properties.put("target_temperature", String.valueOf(target_temperature));
        properties.put("response_speed", String.valueOf(response_speed));
        properties.put("opening_time", String.valueOf(opening_time));
    }
}
