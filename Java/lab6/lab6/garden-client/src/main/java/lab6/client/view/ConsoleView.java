package lab6.client.view;

import lab6.common.dto.DeviceSnapshot;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner in = new Scanner(System.in);

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showDevices(List<DeviceSnapshot> devices) {
        if (devices == null || devices.isEmpty()) {
            System.out.println("Список устройств пуст\n");
            return;
        }

        for (DeviceSnapshot device : devices) {
            System.out.println(device.prettyString());
        }
    }

    public void showStats(String fileName, String objectsFound, String propertiesRead, String propertiesMissing) {
        System.out.println("Текущий файл сервера: " + fileName);
        System.out.println("Количество найденных объектов: " + objectsFound);
        System.out.println("Количество успешно прочитанных свойств: " + propertiesRead);
        System.out.println("Количество отсутствующих свойств: " + propertiesMissing + "\n");
    }

    public void showInteractiveMenu() {
        System.out.println("1 - получить и показать устройства с сервера");
        System.out.println("2 - заменить файл на сервере");
        System.out.println("3 - циклично выводить информацию о состоянии объектов");
        System.out.println("4 - логировать изменения в CSV");
        System.out.println("5 - случайно изменить свойства устройства на сервере");
        System.out.println("6 - включить умный режим сада на сервере");
        System.out.println("7 - обслужить все устройства на сервере");
        System.out.println("8 - показать текущее имя файла на сервере");
        System.out.println("9 - help");
        System.out.println("0 - выход\n");
        System.out.print("Введите номер действия которое вы хотите сделать: ");
    }

    public int readInt() {
        while (true) {
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Введите целое число: ");
            }
        }
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    public void showHelp() {
        System.out.println("Команды клиента:");
        System.out.println("--help");
        System.out.println("--show-once");
        System.out.println("--replace-file --new-file <имя_файла>");
        System.out.println("--watch --period <секунды>");
        System.out.println("--csv-log --period <секунды> --csv <имя_csv>");
        System.out.println("--interactive");
        System.out.println();
        System.out.println("Сеть:");
        System.out.println("Используется TCP и собственный бинарный протокол.");
        System.out.println("Каждое сообщение режется на кадры до 1400 байт, чтобы держаться в рамках MTU.");
        System.out.println("IP сервера и порт настраиваются в application.properties клиента.");
        System.out.println();
    }
}
