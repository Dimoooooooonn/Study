package lab5.controller;

import lab5.model.AutomaticFiring;
import lab5.model.DeviceCsvLogger;
import lab5.model.DeviceFileReader;
import lab5.model.GardenDevice;
import lab5.model.LavnMover;
import lab5.model.ThermalDrive;
import lab5.view.ConsoleView;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class DeviceController {
    private final ConsoleView view;
    private final LinkedList<GardenDevice> devices;
    private final DeviceFileReader file_reader;
    private String file_name;

    public DeviceController(ConsoleView view){
        this.view = view;
        this.devices = new LinkedList<>();
        this.file_name = "devices.txt";
        this.file_reader = new DeviceFileReader(file_name);
    }

    public void setFileName(String file_name){
        this.file_name = file_name;
        file_reader.setFileName(file_name);
    }

    public String getFileName(){
        return file_name;
    }

    public void runInteractive(){
        view.printMenu();
        short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");
        while (choise != 0) {
            switch (choise) {
                case 1:
                    addDevice();
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
                case 9:
                    readDevicesFromFileToMemory();
                    break;
                case 10:
                    changeFileNameInteractive();
                    break;
                case 11:
                    showFileName();
                    break;
                default:
                    view.showMessage("Некорректный пункт меню\n");
                    break;
            }
            view.printMenu();
            choise = view.readShort("Введите номер действия которое вы хотите сделать: ");
        }
        view.showMessage("Выход из программы");
    }

    public void addDevice(){
        view.showMessage("1 - добавить газонокосилку\n" +
                "2 - добавить автополив\n" +
                "3 - добавить термопривод\n" +
                "0 - выход назад");
        short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");
        if (choise == 0) { return; }

        String manufacturer = view.readString("Производитель: ");
        String model = view.readString("Модель: ");
        String power = view.readString("Источник питания: ");
        boolean powered_on = false;
        short intensivity = (short) view.readInt("Интенсивность работы (от 1 до 4): ");
        int wear = view.readInt("Износ (от 0 до 100): ");

        GardenDevice device;

        switch (choise) {
            case 1:
                int container_level = view.readInt("Заполненность травосборника (0-100): ");
                int blade_sharpness = view.readInt("Острота ножей (0-100): ");
                int cut_height = view.readInt("Высота скашивания (2-15 см): ");
                device = new LavnMover(manufacturer, model, power, powered_on, intensivity, wear, container_level, blade_sharpness, cut_height);
                break;
            case 2:
                int water_tank_level = view.readInt("Уровень воды в баке (0-100): ");
                int watering_duration = view.readInt("Длительность полива (1-120 мин): ");
                int soil_moisture = view.readInt("Влажность почвы (0-100): ");
                int active_zone = view.readInt("Активная зона полива (1-10): ");
                device = new AutomaticFiring(manufacturer, model, power, powered_on, intensivity, wear, water_tank_level, watering_duration, soil_moisture, active_zone);
                break;
            case 3:
                int temperature = view.readInt("Температура в теплице (0-60): ");
                int window_opening = view.readInt("Открытие окна (0-100): ");
                int target_temperature = view.readInt("Целевая температура (10-35): ");
                int response_speed = view.readInt("Скорость реакции (2-20): ");
                device = new ThermalDrive(manufacturer, model, power, powered_on, intensivity, wear, temperature, window_opening, target_temperature, response_speed);
                break;
            default:
                view.showMessage("Некорректный тип устройства\n");
                return;
        }

        devices.add(device);
        view.showMessage("Устройство добавлено:");
        view.showDevices(java.util.List.of(device));
    }

    public void showInfo() {
        view.showDevices(devices);
    }

    public void deleteDevice() {
        if (devices.isEmpty()) {
            view.showMessage("Список устройств пуст.\n");
            return;
        }

        int id = view.readInt("Введите id устройства для удаления: ");
        for (int i = 0; i < devices.size(); i++) {
            if (id == devices.get(i).getId()) {
                devices.remove(i);
                view.showMessage("Устройство удалено.\n");
                return;
            }
        }
        view.showMessage("Устройство с таким id не найдено.\n");
    }

    public void editDevice() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        view.showMessage("1 - изменить производителя\n" +
                "2 - изменить модель\n" +
                "3 - изменить источник питания\n" +
                "4 - изменить интенсивность работы\n" +
                "5 - изменить износ\n" +
                "6 - изменить специальные свойства\n" +
                "0 - выход назад");
        short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

        switch (choise) {
            case 1:
                device.setDeviceManufacturer(view.readString("Новый производитель: "));
                break;
            case 2:
                device.setDeviceModel(view.readString("Новая модель: "));
                break;
            case 3:
                device.setPowerSupply(view.readString("Новый источник питания: "));
                break;
            case 4:
                device.setWorkIntensity((short) view.readInt("Новая интенсивность (1-4): "));
                break;
            case 5:
                device.setWearLevel(view.readInt("Новый износ (0-100): "));
                break;
            case 6:
                editSpecificProperties(device);
                break;
            case 0:
                return;
            default:
                view.showMessage("Некорректный пункт меню\n");
                return;
        }

        view.showMessage("Свойства устройства изменены:");
        view.showDevices(java.util.List.of(device));
    }

    private void editSpecificProperties(GardenDevice device) {
        if (device instanceof LavnMover mower) {
            view.showMessage("1 - изменить заполненность травосборника\n" +
                    "2 - изменить остроту ножей\n" +
                    "3 - изменить высоту скашивания\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    mower.setGrassContainerLevel(view.readInt("Новое значение: "));
                    break;
                case 2:
                    mower.setBladeSharpness(view.readInt("Новое значение: "));
                    break;
                case 3:
                    mower.setCutHeight(view.readInt("Новое значение: "));
                    break;
                default:
                    break;
            }
        }
        else if (device instanceof AutomaticFiring firing) {
            view.showMessage("1 - изменить уровень воды в баке\n" +
                    "2 - изменить длительность полива\n" +
                    "3 - изменить влажность почвы\n" +
                    "4 - изменить активную зону\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    firing.setWaterTankLevel(view.readInt("Новое значение: "));
                    break;
                case 2:
                    firing.setWateringDuration(view.readInt("Новое значение: "));
                    break;
                case 3:
                    firing.setSoilMoisture(view.readInt("Новое значение: "));
                    break;
                case 4:
                    firing.setActiveZone(view.readInt("Новое значение: "));
                    break;
                default:
                    break;
            }
        }
        else if (device instanceof ThermalDrive drive) {
            view.showMessage("1 - изменить температуру в теплице\n" +
                    "2 - изменить открытие окна\n" +
                    "3 - изменить целевую температуру\n" +
                    "4 - изменить скорость реакции\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    drive.setGreenhouseTemperature(view.readInt("Новое значение: "));
                    break;
                case 2:
                    drive.setWindowOpeningPercent(view.readInt("Новое значение: "));
                    break;
                case 3:
                    drive.setTargetTemperature(view.readInt("Новое значение: "));
                    break;
                case 4:
                    drive.setResponseSpeed(view.readInt("Новое значение: "));
                    break;
                default:
                    break;
            }
        }
    }

    public void deviceWork() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        view.showMessage("1 - включить устройство\n" +
                "2 - выключить устройство\n" +
                "3 - выполнить работу устройства\n" +
                "4 - показать состояние устройства\n" +
                "5 - изменить интенсивность работы\n" +
                "6 - специальные действия устройства\n" +
                "0 - выход назад");
        short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

        switch (choise) {
            case 1:
                view.showMessage(device.turnOnDevice() + "\n");
                break;
            case 2:
                view.showMessage(device.turnOffDevice() + "\n");
                break;
            case 3:
                view.showMessage(device.deviceOperation() + "\n");
                break;
            case 4:
                view.showDevices(java.util.List.of(device));
                break;
            case 5:
                short intensivity = (short) view.readInt("Введите интенсивность работы (1-4): ");
                view.showMessage(device.changeWorkIntensivity(intensivity) + "\n");
                break;
            case 6:
                deviceSpecialWork(device);
                break;
            case 0:
                return;
            default:
                view.showMessage("Некорректный пункт меню\n");
                break;
        }
    }

    private void deviceSpecialWork(GardenDevice device) {
        if (device instanceof LavnMover mower) {
            view.showMessage("1 - заточить ножи\n" +
                    "2 - очистить травосборник\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    view.showMessage(mower.sharpenBlades());
                    break;
                case 2:
                    view.showMessage(mower.emptyGrassContainer());
                    break;
                default:
                    break;
            }
            view.showMessage("");
        }
        else if (device instanceof AutomaticFiring firing) {
            view.showMessage("1 - наполнить бак\n" +
                    "2 - переключить зону полива\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    view.showMessage(firing.refillTank());
                    break;
                case 2:
                    view.showMessage(firing.switchZone(view.readInt("Введите новую зону: ")));
                    break;
                default:
                    break;
            }
            view.showMessage("");
        }
        else if (device instanceof ThermalDrive drive) {
            view.showMessage("1 - откалибровать датчик\n" +
                    "2 - аварийно открыть окно\n" +
                    "3 - закрыть окно\n" +
                    "0 - выход назад");
            short choise = view.readShort("Введите номер действия которое вы хотите сделать: ");

            switch (choise) {
                case 1:
                    view.showMessage(drive.calibrateSensor());
                    break;
                case 2:
                    view.showMessage(drive.emergencyOpenWindow());
                    break;
                case 3:
                    view.showMessage(drive.closeWindow());
                    break;
                default:
                    break;
            }
            view.showMessage("");
        }
    }

    public void randomizeDevice() {
        GardenDevice device = findDevice();
        if (device == null) { return; }

        device.randomizeSpecificProperties();
        view.showMessage("Свойства устройства изменены случайным образом:");
        view.showDevices(java.util.List.of(device));
    }

    public void smartGardenMode() {
        if (devices.isEmpty()) {
            view.showMessage("Список устройств пуст.\n");
            return;
        }

        view.showMessage("Запуск умного режима сада\n");

        for (GardenDevice device: devices) {
            if (!device.getPoweredOn()) {
                device.turnOnDevice();
            }
            if (device.getWorkIntensity() == 0) {
                device.changeWorkIntensivity((short) 1);
            }
            view.showMessage(device.deviceOperation());
        }
        showInfo();
    }

    public void serviceAllDevices() {
        if (devices.isEmpty()) {
            view.showMessage("Список устройств пуст.\n");
            return;
        }

        for (GardenDevice device: devices) {
            view.showMessage(device.serviceDevice());
        }
        view.showMessage("");
    }

    public void readDevicesFromFileToMemory(){
        LinkedList<GardenDevice> read_devices = file_reader.readDevices();

        if (read_devices.isEmpty()) {
            view.showMessage("Из файла не было прочитано ни одного устройства\n");
            view.showFileReadStatistics(file_reader.getFoundObjects(), file_reader.getFoundProperties(), file_reader.getMissingProperties());
            return;
        }

        devices.addAll(read_devices);
        view.showMessage("Из файла прочитано устройств: " + read_devices.size());
        view.showFileReadStatistics(file_reader.getFoundObjects(), file_reader.getFoundProperties(), file_reader.getMissingProperties());
    }

    public LinkedList<GardenDevice> readDevicesOnce(){
        return file_reader.readDevices();
    }

    public void showDevicesFromFileOnce(){
        LinkedList<GardenDevice> read_devices = file_reader.readDevices();
        if (read_devices.isEmpty()) {
            view.showMessage("Из файла не было прочитано ни одного устройства\n");
            view.showFileReadStatistics(file_reader.getFoundObjects(), file_reader.getFoundProperties(), file_reader.getMissingProperties());
            return;
        }

        view.showMessage("Разовый вывод состояния объектов из файла");
        view.showDevices(read_devices);
        view.showFileReadStatistics(file_reader.getFoundObjects(), file_reader.getFoundProperties(), file_reader.getMissingProperties());
    }

    public void replaceFile(String new_file_name){
        String old_file_name = file_name;
        setFileName(new_file_name);
        view.showMessage("Файл заменен");
        view.showMessage("Старый файл: " + old_file_name);
        view.showMessage("Новый файл: " + file_name + "\n");
    }

    public void watchFile(int period_seconds){
        if (period_seconds <= 0) {
            view.showMessage("Период должен быть больше 0");
            return;
        }

        view.showMessage("Запущен цикличный вывод состояния объектов. Для остановки нажмите Ctrl+C\n");
        while (true) {
            view.showMessage("Время чтения: " + LocalDateTime.now());
            showDevicesFromFileOnce();

            try {
                Thread.sleep(period_seconds * 1000L);
            }
            catch (InterruptedException e){
                view.showMessage("Режим цикличного вывода остановлен");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void csvLogChanges(int period_seconds, String csv_file_name){
        if (period_seconds <= 0) {
            view.showMessage("Период должен быть больше 0");
            return;
        }

        DeviceCsvLogger logger = new DeviceCsvLogger(csv_file_name);
        LinkedList<GardenDevice> old_devices = file_reader.readDevices();

        view.showMessage("Запущено логирование изменений в CSV файл: " + logger.getCsvFileName());
        view.showMessage("Для остановки нажмите Ctrl+C\n");

        while (true) {
            try {
                Thread.sleep(period_seconds * 1000L);
            }
            catch (InterruptedException e){
                view.showMessage("Логирование остановлено");
                Thread.currentThread().interrupt();
                return;
            }

            LinkedList<GardenDevice> new_devices = file_reader.readDevices();
            int changes_count = logger.logChanges(old_devices, new_devices);
            view.showMessage("Изменений записано в CSV: " + changes_count);
            old_devices = new_devices;
        }
    }

    public void changeFileNameInteractive(){
        setFileName(view.readString("Введите новое название файла: "));
        view.showMessage("Новое название файла установлено: " + file_name + "\n");
    }

    public void showFileName(){
        view.showMessage("Текущее название файла: " + file_name + "\n");
    }

    private GardenDevice findDevice() {
        if (devices.isEmpty()) {
            view.showMessage("Список устройств пуст.\n");
            return null;
        }

        int id = view.readInt("Введите id устройства: ");
        for (GardenDevice device: devices) {
            if (device.getId() == id) {
                return device;
            }
        }

        view.showMessage("Устройство с таким id не найдено.\n");
        return null;
    }
}
