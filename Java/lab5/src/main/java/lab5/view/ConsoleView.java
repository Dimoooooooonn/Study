package lab5.view;

import lab5.model.GardenDevice;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner in;

    public ConsoleView(){
        in = new Scanner(System.in);
    }

    public void printMenu(){
        System.out.println(
                "1 - добавить устройство\n" +
                        "2 - показать все устройства\n" +
                        "3 - удалить устройство\n" +
                        "4 - изменить свойства устройства\n" +
                        "5 - Функциональная работа с устройством\n" +
                        "6 - Случайно изменить свойства устройства\n" +
                        "7 - Умный режим сада\n" +
                        "8 - Обслужить все устройства\n" +
                        "9 - прочитать устройства из файла\n" +
                        "10 - изменить название файла\n" +
                        "11 - показать текущее название файла\n" +
                        "0 - выход из программы\n");
    }

    public void showMessage(String message){
        System.out.println(message);
    }

    public void showDevices(List<GardenDevice> devices){
        if (devices == null || devices.isEmpty()) {
            System.out.println("Список устройств пуст\n");
            return;
        }
        for (GardenDevice device: devices) {
            System.out.println(device.buildInfo());
        }
        System.out.println();
    }

    public short readShort(String message){
        while (true) {
            try {
                System.out.print(message);
                return Short.parseShort(in.nextLine().trim());
            }
            catch (NumberFormatException e){
                System.out.println("Нужно ввести целое число");
            }
        }
    }

    public int readInt(String message){
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(in.nextLine().trim());
            }
            catch (NumberFormatException e){
                System.out.println("Нужно ввести целое число");
            }
        }
    }

    public String readString(String message){
        while (true) {
            System.out.print(message);
            String value = in.nextLine().trim();
            if (!value.isEmpty()) { return value; }
            System.out.println("Строка не должна быть пустой");
        }
    }

    public void showFileReadStatistics(int found_objects, int found_properties, int missing_properties){
        System.out.println("Количество найденных объектов: " + found_objects);
        System.out.println("Количество успешно прочитанных свойств: " + found_properties);
        System.out.println("Количество не найденных свойств: " + missing_properties + "\n");
    }

    public void showHelp(){
        System.out.println("Справка по ключам запуска:\n");
        System.out.println("--help");
        System.out.println("    показать справку\n");
        System.out.println("--show-once --file <имя_файла>");
        System.out.println("    разовый вывод информации о состоянии объектов из файла\n");
        System.out.println("--replace-file --file <старый_файл> --new-file <новый_файл>");
        System.out.println("    замена текущего файла другим и разовый вывод новых данных\n");
        System.out.println("--watch --file <имя_файла> --period <секунды>");
        System.out.println("    цикличный вывод информации о состоянии объектов с периодическим перечитыванием файла\n");
        System.out.println("--csv-log --file <имя_файла> --csv <имя_csv> --period <секунды>");
        System.out.println("    логирование изменяющихся свойств в CSV файл с указанием времени\n");
        System.out.println("--interactive --file <имя_файла>");
        System.out.println("    режим работы аналогичный ЛР4 через консольное меню\n");
    }
}
