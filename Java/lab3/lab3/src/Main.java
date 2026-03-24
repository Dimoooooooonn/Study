import java.util.LinkedList;
import java.util.Scanner;


public class Main{
    public static Scanner in = new Scanner(System.in);
    public static LinkedList<GardenDevice> devices = new LinkedList<>();

    public static void main(String[] args){
        printMenu();
        short choise = in.nextShort();
        while (choise != 0) {
            switch (choise) {
                case 1:
                    AddDevice();
                    break;
                case 2:
                    showInfo();
                    break;
                case 3:
                    deleteDevice();
                    break;
                case 4:
                    editDevice();
                    break;
                case 5:
                    deviceWork();
                    break;
                case 6:
                    randomizeDevice();
                    break;
                case 7:
                    smartGardenMode();
                    break;
                case 8:
                    serviceAllDevices();
                    break;
                default:
                    System.out.println("Некорректный пункт меню\n");
                    break;
            }
            printMenu();
            choise = in.nextShort();
        }
    }

    public static void printMenu(){
        System.out.println(
                "1 - добавить устройство\n" +
                        "2 - показать все устройства\n" +
                        "3 - удалить устройство\n" +
                        "4 - изменить свойства устройства\n" +
                        "5 - Функциональная работа с устройством\n" +
                        "6 - Случайно изменить свойства устройства\n" +
                        "7 - Умный режим сада\n" +
                        "8 - Обслужить все устройства\n" +
                        "0 - выход из программы\n");

        System.out.print("Введите номер действия которое вы хотите сделать: ");
    }

    public static void AddDevice(){
        System.out.println(
                "1 - добавить газонокосилку\n" +
                        "2 - добавить автополив\n" +
                        "3 - добавить термопривод\n" +
                        "0 - выход назад");

        System.out.print("Введите номер действия которое вы хотите сделать: ");
        short choise = in.nextShort();
        if (choise == 0) { return; }
        System.out.print("Производитель: ");
        String manufacturer = in.next();
        System.out.print("Модель: ");
        String model = in.next();
        System.out.print("Источник питания: ");
        String power = in.next();
        boolean powered_on = false;
        System.out.print("Интенсивность работы (от 1 до 4):  ");
        short intensivity = in.nextShort();
        System.out.print("Износ (от 0 до 100): ");
        int wear = in.nextInt();

        GardenDevice device;

        switch (choise) {
            case 1:
                System.out.println("Заполненность травосборника (0-100): ");
                int container_level = in.nextInt();
                System.out.println("Острота ножей (0-100): ");
                int blade_sharpness = in.nextInt();
                System.out.println("Высота скашивания (2-15 см): ");
                int cut_height = in.nextInt();
                device = new LavnMover(manufacturer, model, power, powered_on, intensivity, wear, container_level, blade_sharpness, cut_height);
                break;
            case 2:
                System.out.println("Уровень воды в баке (0-100): ");
                int water_tank_level = in.nextInt();
                System.out.println("Длительность полива (1-120 мин): ");
                int watering_duration = in.nextInt();
                System.out.println("Влажность почвы (0-100): ");
                int soil_moisture = in.nextInt();
                System.out.println("Активная зона полива (1-10): ");
                int active_zone = in.nextInt();
                device = new AutomaticFiring(manufacturer, model, power, powered_on, intensivity, wear, water_tank_level, watering_duration, soil_moisture, active_zone);
                break;
            case 3:
                System.out.println("Температура в теплице (0-60): ");
                int temperature = in.nextInt();
                System.out.println("Открытие окна (0-100): ");
                int window_opening = in.nextInt();
                System.out.println("Целевая температура (10-35): ");
                int target_temperature = in.nextInt();
                System.out.println("Скорость реакции (2-20): ");
                int response_speed = in.nextInt();
                device = new ThermalDrive(manufacturer, model, power, powered_on, intensivity, wear, temperature, window_opening, target_temperature, response_speed);
                break;
            default:
                System.out.println("Некорректный тип устройства");
                return;
        }

        devices.add(device);
        System.out.println("Устройство добавлено:\n" + device.id);
    }

    public static void showInfo() {
        if (devices.isEmpty()) {
            System.out.println("Список устройств пуст");
            return;
        }
        for (GardenDevice el: devices) { el.printInfo(); }
    }

    public static void deleteDevice() {
        if (devices.isEmpty()) {
            System.out.println("Список устройств пуст.");
            return;
        }

        System.out.print("Введите id устройства для удаления: ");
        int id = in.nextInt();
        for (int i = 0; i < devices.size(); i++) {
            if (id == devices.get(i).id) {
                devices.remove(i);
                System.out.println("Устройство удалено.\n");
                return;
            }
        }
    }

    public static void editDevice() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        System.out.println(
                "1 - изменить производителя\n" +
                        "2 - изменить модель\n" +
                        "3 - изменить источник питания\n" +
                        "4 - изменить интенсивность работы\n" +
                        "5 - изменить износ\n" +
                        "6 - изменить специальные свойства\n" +
                        "0 - выход назад");

        System.out.print("Введите номер действия которое вы хотите сделать: ");
        short choise = in.nextShort();

        switch (choise) {
            case 1:
                System.out.print("Новый производитель: ");
                device.device_manufacturer = in.next();
                break;
            case 2:
                System.out.print("Новая модель: ");
                device.device_model = in.next();
                break;
            case 3:
                System.out.print("Новый источник питания: ");
                device.power_supply = in.next();
                break;
            case 4:
                System.out.print("Новая интенсивность (1-4): ");
                device.work_intensity = in.nextShort();
                break;
            case 5:
                System.out.print("Новый износ (0-100): ");
                device.wear_level = in.nextInt();
                break;
            case 6:
                editSpecificProperties(device);
                break;
            case 0:
                return;
            default:
                System.out.println("Некорректный пункт меню\n");
                return;
        }

        System.out.println("Свойства устройства изменены:");
        device.printInfo();
        System.out.println();
    }

    public static void editSpecificProperties(GardenDevice device) {
        if (device instanceof LavnMover) {
            LavnMover mower = (LavnMover) device;
            System.out.println(
                    "1 - изменить заполненность травосборника\n" +
                            "2 - изменить остроту ножей\n" +
                            "3 - изменить высоту скашивания\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    System.out.print("Новое значение: ");
                    mower.setGrassContainerLevel(in.nextInt());
                    break;
                case 2:
                    System.out.print("Новое значение: ");
                    mower.setBladeSharpness(in.nextInt());
                    break;
                case 3:
                    System.out.print("Новое значение: ");
                    mower.setCutHeight(in.nextInt());
                    break;
                default:
                    break;
            }
        }
        else if (device instanceof AutomaticFiring) {
            AutomaticFiring firing = (AutomaticFiring) device;
            System.out.println(
                    "1 - изменить уровень воды в баке\n" +
                            "2 - изменить длительность полива\n" +
                            "3 - изменить влажность почвы\n" +
                            "4 - изменить активную зону\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    System.out.print("Новое значение: ");
                    firing.setWaterTankLevel(in.nextInt());
                    break;
                case 2:
                    System.out.print("Новое значение: ");
                    firing.setWateringDuration(in.nextInt());
                    break;
                case 3:
                    System.out.print("Новое значение: ");
                    firing.setSoilMoisture(in.nextInt());
                    break;
                case 4:
                    System.out.print("Новое значение: ");
                    firing.setActiveZone(in.nextInt());
                    break;
                default:
                    break;
            }
        }
        else if (device instanceof ThermalDrive) {
            ThermalDrive drive = (ThermalDrive) device;
            System.out.println(
                    "1 - изменить температуру в теплице\n" +
                            "2 - изменить открытие окна\n" +
                            "3 - изменить целевую температуру\n" +
                            "4 - изменить скорость реакции\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    System.out.print("Новое значение: ");
                    drive.setGreenhouseTemperature(in.nextInt());
                    break;
                case 2:
                    System.out.print("Новое значение: ");
                    drive.setWindowOpeningPercent(in.nextInt());
                    break;
                case 3:
                    System.out.print("Новое значение: ");
                    drive.setTargetTemperature(in.nextInt());
                    break;
                case 4:
                    System.out.print("Новое значение: ");
                    drive.setResponseSpeed(in.nextInt());
                    break;
                default:
                    break;
            }
        }
    }

    public static void deviceWork() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        System.out.println(
                "1 - включить устройство\n" +
                        "2 - выключить устройство\n" +
                        "3 - выполнить работу устройства\n" +
                        "4 - показать состояние устройства\n" +
                        "5 - изменить интенсивность работы\n" +
                        "6 - специальные действия устройства\n" +
                        "0 - выход назад");

        System.out.print("Введите номер действия которое вы хотите сделать: ");
        short choise = in.nextShort();

        switch (choise) {
            case 1:
                device.turnOnDevice();
                System.out.println("Устройство включено\n");
                break;
            case 2:
                device.turnOffDevice();
                System.out.println("Устройство выключено\n");
                break;
            case 3:
                device.deviceOperation();
                System.out.println();
                break;
            case 4:
                device.printInfo();
                System.out.println();
                break;
            case 5:
                System.out.print("Введите интенсивность работы (1-4): ");
                short intensivity = in.nextShort();
                device.work_intensity = intensivity;
                device.changeWorkIntensivity(intensivity);
                System.out.println();
                break;
            case 6:
                deviceSpecialWork(device);
                break;
            case 0:
                return;
            default:
                System.out.println("Некорректный пункт меню\n");
                break;
        }
    }

    public static void deviceSpecialWork(GardenDevice device) {
        if (device instanceof LavnMover) {
            LavnMover mower = (LavnMover) device;
            System.out.println(
                    "1 - заточить ножи\n" +
                            "2 - очистить травосборник\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    mower.sharpenBlades();
                    break;
                case 2:
                    mower.emptyGrassContainer();
                    break;
                default:
                    break;
            }
            System.out.println();
        }
        else if (device instanceof AutomaticFiring) {
            AutomaticFiring firing = (AutomaticFiring) device;
            System.out.println(
                    "1 - наполнить бак\n" +
                            "2 - переключить зону полива\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    firing.refillTank();
                    break;
                case 2:
                    System.out.print("Введите новую зону: ");
                    firing.switchZone(in.nextInt());
                    break;
                default:
                    break;
            }
            System.out.println();
        }
        else if (device instanceof ThermalDrive) {
            ThermalDrive drive = (ThermalDrive) device;
            System.out.println(
                    "1 - откалибровать датчик\n" +
                            "2 - аварийно открыть окно\n" +
                            "3 - закрыть окно\n" +
                            "0 - выход назад");
            System.out.print("Введите номер действия которое вы хотите сделать: ");
            short choise = in.nextShort();

            switch (choise) {
                case 1:
                    drive.calibrateSensor();
                    break;
                case 2:
                    drive.emergencyOpenWindow();
                    break;
                case 3:
                    drive.closeWindow();
                    break;
                default:
                    break;
            }
            System.out.println();
        }
    }

    public static void randomizeDevice() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        device.randomizeSpecificProperties();
        System.out.println("Свойства устройства изменены случайным образом:");
        device.printInfo();
        System.out.println();
    }

    public static void smartGardenMode() {
        if (devices.isEmpty()) {
            System.out.println("Список устройств пуст.\n");
            return;
        }

        System.out.println("Запуск умного режима сада\n");

        for (GardenDevice el: devices) {
            if (!el.powered_on) {
                el.turnOnDevice();
            }
            if (el.work_intensity == 0) {
                el.work_intensity = 1;
                el.changeWorkIntensivity((short)1);
            }
            el.deviceOperation();
            el.printInfo();
            System.out.println();
        }
    }

    public static void serviceAllDevices() {
        if (devices.isEmpty()) {
            System.out.println("Список устройств пуст.\n");
            return;
        }

        for (GardenDevice el: devices) {
            el.serviceDevice();
        }
        System.out.println();
    }

    public static GardenDevice findDevice() {
        if (devices.isEmpty()) {
            System.out.println("Список устройств пуст.\n");
            return null;
        }

        System.out.print("Введите id устройства: ");
        int id = in.nextInt();

        for (GardenDevice el: devices) {
            if (el.id == id) {
                return el;
            }
        }

        System.out.println("Устройство с таким id не найдено.\n");
        return null;
    }

}