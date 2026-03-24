package lab6.server.model;

import java.util.Map;

public class LavnMover extends GardenDevice {
    private int grass_container_level;
    private int blade_sharpness;
    private int cut_height;
    private int velocity;

    public LavnMover() {
        super();
        grass_container_level = 0;
        blade_sharpness = 0;
        cut_height = 0;
        velocity = 0;
    }

    public LavnMover(String device_manufacturer, String device_model, String power_supply, boolean powered_on,
                     short work_intensity, int wear_level, int grass_container_level,
                     int blade_sharpness, int cut_height) {
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.grass_container_level = grass_container_level;
        this.blade_sharpness = blade_sharpness;
        this.cut_height = cut_height;
        velocity = 0;
    }

    @Override
    public String changeWorkIntensivity(short intensivity) {
        if (!powered_on) {
            work_intensity = intensivity;
            return "Прежде чем поменять скорость включите газонокосилку";
        }
        if (intensivity >= 1 && intensivity <= 4) {
            work_intensity = intensivity;
            switch (intensivity) {
                case 1 -> velocity = 2;
                case 2 -> velocity = 3;
                case 3 -> velocity = 4;
                case 4 -> velocity = 7;
                default -> velocity = 0;
            }
            return "Скорость газонокосилки составляет: " + velocity + " км/ч";
        }
        return "Некорректная интенсивность";
    }

    @Override
    public String deviceOperation() {
        if (!powered_on) {
            return "Газонокосилка выключена. Сначала включите устройство";
        }
        if (wear_level >= 90) {
            return "Газонокосилка слишком изношена. Нужен ремонт";
        }
        if (blade_sharpness < 25) {
            return "Ножи слишком тупые. Требуется заточка";
        }
        if (grass_container_level >= 95) {
            return "Травосборник переполнен. Сначала очистите его";
        }

        int grass = randomValue(12, 25);
        int sharpnessDecrease = randomValue(5, 12);
        int wearIncrease = randomValue(4, 9);

        grass_container_level = clampValue(grass_container_level + grass, 0, 100);
        blade_sharpness = clampValue(blade_sharpness - sharpnessDecrease, 0, 100);
        wear_level = clampValue(wear_level + wearIncrease, 0, 100);
        return "Газонокосилка косит траву с высотой среза " + cut_height + " см";
    }

    public String sharpenBlades() {
        blade_sharpness = 100;
        return "Ножи заточены. Острота восстановлена до 100";
    }

    public String emptyGrassContainer() {
        grass_container_level = 0;
        return "Травосборник очищен";
    }

    @Override
    public void randomizeSpecificProperties() {
        grass_container_level = clampValue(grass_container_level + randomValue(-15, 15), 0, 100);
        blade_sharpness = clampValue(blade_sharpness + randomValue(-10, 5), 0, 100);
        cut_height = clampValue(cut_height + randomValue(-2, 2), 2, 15);
    }

    @Override
    protected void fillSpecificProperties(Map<String, String> properties) {
        properties.put("grass_container_level", String.valueOf(grass_container_level));
        properties.put("blade_sharpness", String.valueOf(blade_sharpness));
        properties.put("cut_height", String.valueOf(cut_height));
        properties.put("velocity", String.valueOf(velocity));
    }
}
