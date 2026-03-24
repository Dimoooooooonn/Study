import static java.lang.Math.clamp;


public class ThermalDrive extends GardenDevice{
    private int greenhouse_temperature;
    private int window_opening_percent;
    private int target_temperature;
    private int response_speed;

    public ThermalDrive(){
        super();
        greenhouse_temperature = 0;
        window_opening_percent = 0;
        target_temperature = 0;
        response_speed = 0;
    }

    public ThermalDrive(String device_manufacturer, String device_model, String power_supply, boolean powered_on, short work_intensity, int wear_level,
                        int greenhouse_temperature, int window_opening_percent, int target_temperature, int response_speed){
        super(device_manufacturer, device_model, power_supply, powered_on, work_intensity, wear_level);
        this.greenhouse_temperature = greenhouse_temperature;
        this.window_opening_percent = window_opening_percent;
        this.target_temperature = target_temperature;
        this.response_speed = response_speed;
    }

    public void setGreenhouseTemperature(int greenhouse_temperature) { this.greenhouse_temperature = greenhouse_temperature; }
    public void setWindowOpeningPercent(int window_opening_percent) { this.window_opening_percent = window_opening_percent; }
    public void setTargetTemperature(int target_temperature) { this.target_temperature = target_temperature; }
    public void setResponseSpeed(int response_speed) { this.response_speed = response_speed; }

    @Override
    public void changeWorkIntensivity(short intensivity) {
        if (!powered_on) {
            System.out.println("Прежде чем поменять скорость реагирования термопривода включите устройство");
        } else if (intensivity == 1 || intensivity == 2 || intensivity == 3 || intensivity == 4) {
            switch (intensivity) {
                case 1:
                    response_speed = 2;
                    System.out.println("Проверка показателей будет через каждые: " + response_speed + " минут");
                    break;
                case 2:
                    response_speed = 5;
                    System.out.println("Проверка показателей будет через каждые: " + response_speed + " минут");
                    break;
                case 3:
                    response_speed = 10;
                    System.out.println("Проверка показателей будет через каждые: " + response_speed + " минут");
                    break;
                case 4:
                    response_speed = 20;
                    System.out.println("Проверка показателей будет через каждые: " + response_speed + " минут");
                    break;
                default:
                    response_speed = 0;
                    System.out.println("Проверка показателей будет через каждые: " + response_speed + " минут");
                    break;
            }
        }
    }

    @Override
    public void deviceOperation(){
        if (!powered_on) {
            System.out.println("Термопривод выключен. Сначала включите устройство");
            return;
        }
        if (wear_level >= 90) {
            System.out.println("Термопривод слишком изношен. Нужен ремонт");
            return;
        }

        if (greenhouse_temperature > target_temperature) {
            int open_delta = response_speed * 5;
            int cool_delta = randomValue(2, 5);
            window_opening_percent = clamp(window_opening_percent + open_delta, 0, 100);
            greenhouse_temperature = clamp(greenhouse_temperature - cool_delta, 0, 60);
            wear_level = clamp(wear_level + randomValue(1, 4), 0, 100);
            System.out.println("Термопривод открывает окно для охлаждения теплицы");
        } else if (greenhouse_temperature < target_temperature - 2) {
            int close_delta = response_speed * 8;
            int warm_delta = randomValue(1, 3);
            window_opening_percent = clamp(window_opening_percent - close_delta, 0, 100);
            greenhouse_temperature = clamp(greenhouse_temperature + warm_delta, 0, 60);
            wear_level = clamp(wear_level + randomValue(1, 3), 0, 100);
            System.out.println("Термопривод прикрывает окно для сохранения тепла");
        } else {
            System.out.println("Температура близка к целевой. Коррекция не требуется");
        }
    }

    public void calibrateSensor() {
        greenhouse_temperature = clamp(greenhouse_temperature + randomValue(-1, 1), 0, 60);
        System.out.println("Датчик температуры откалиброван");
    }

    public void emergencyOpenWindow() {
        window_opening_percent = 100;
        System.out.println("Окно аварийно открыто на 100%");
    }

    public void closeWindow() {
        window_opening_percent = 0;
        System.out.println("Окно полностью закрыто");
    }

    @Override
    public void randomizeSpecificProperties() {
        greenhouse_temperature = clamp(greenhouse_temperature + randomValue(-4, 4), 0, 60);
        window_opening_percent = clamp(window_opening_percent + randomValue(-20, 20), 0, 100);
        target_temperature = clamp(target_temperature + randomValue(-2, 2), 10, 35);
        response_speed = clamp(response_speed + randomValue(-1, 1), 1, 10);
    }

    @Override
    protected String specificInfo() {
        return "Температура в теплице=" + greenhouse_temperature +
                "\nОткрытие окна=" + window_opening_percent + "%" +
                "\nЦелевая температура=" + target_temperature +
                "\nСкорость реакции=" + response_speed  + "\n";
    }
}
