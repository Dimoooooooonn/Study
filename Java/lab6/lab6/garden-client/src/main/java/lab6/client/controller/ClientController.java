package lab6.client.controller;

import lab6.client.network.ServerClient;
import lab6.client.util.DeviceCsvLogger;
import lab6.client.view.ConsoleView;
import lab6.common.dto.DeviceSnapshot;
import lab6.common.protocol.ProtocolTextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    private final ConsoleView view;
    private final ServerClient serverClient;

    public ClientController(ConsoleView view, ServerClient serverClient) {
        this.view = view;
        this.serverClient = serverClient;
    }

    public void run(String[] args) {
        if (args == null || args.length == 0) {
            view.showHelp();
            return;
        }

        String command = args[0];
        Map<String, String> options = parseOptions(args);

        switch (command) {
            case "--help", "help" -> view.showHelp();
            case "--show-once" -> showOnce();
            case "--replace-file" -> replaceFile(options.get("--new-file"));
            case "--watch" -> watch(parseInt(options.get("--period"), -1));
            case "--csv-log" -> csvLog(parseInt(options.get("--period"), -1), options.get("--csv"));
            case "--interactive" -> interactive();
            default -> {
                view.showMessage("Неизвестный ключ запуска\n");
                view.showHelp();
            }
        }
    }

    public void interactive() {
        int choice;
        do {
            view.showInteractiveMenu();
            choice = view.readInt();
            switch (choice) {
                case 1 -> showOnce();
                case 2 -> replaceFile(view.readLine("Введите имя нового файла на сервере: "));
                case 3 -> watch(parseInt(view.readLine("Введите период в секундах: "), -1));
                case 4 -> csvLog(parseInt(view.readLine("Введите период в секундах: "), -1),
                        view.readLine("Введите имя CSV файла: "));
                case 5 -> randomizeDevice(parseInt(view.readLine("Введите id устройства: "), -1));
                case 6 -> smartMode();
                case 7 -> serviceAll();
                case 8 -> currentFile();
                case 9 -> view.showHelp();
                case 0 -> view.showMessage("Выход из программы клиента");
                default -> view.showMessage("Некорректный пункт меню\n");
            }
        } while (choice != 0);
    }

    public void showOnce() {
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "SHOW_ONCE");
        handleAndPrint(request, true);
    }

    public void replaceFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            view.showMessage("Для замены файла нужно указать имя нового файла\n");
            return;
        }
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "REPLACE_FILE");
        request.put("new_file", fileName);
        handleAndPrint(request, true);
    }

    public void currentFile() {
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "GET_CURRENT_FILE");
        ProtocolTextMessage response = sendRequest(request);
        if (response == null) {
            return;
        }
        view.showMessage("Текущее имя файла на сервере: " + response.get("current_file") + "\n");
    }

    public void randomizeDevice(int id) {
        if (id <= 0) {
            view.showMessage("Некорректный id устройства\n");
            return;
        }
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "RANDOMIZE_DEVICE");
        request.put("id", String.valueOf(id));
        handleAndPrint(request, true);
    }

    public void smartMode() {
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "SMART_MODE");
        handleAndPrint(request, true);
    }

    public void serviceAll() {
        ProtocolTextMessage request = new ProtocolTextMessage();
        request.put("command", "SERVICE_ALL");
        handleAndPrint(request, true);
    }

    public void watch(int period) {
        if (period <= 0) {
            view.showMessage("Период должен быть больше нуля\n");
            return;
        }

        view.showMessage("Запущен режим цикличного вывода. Для остановки нажмите Ctrl + C\n");
        while (true) {
            showOnce();
            sleep(period);
        }
    }

    public void csvLog(int period, String csvFile) {
        if (period <= 0 || csvFile == null || csvFile.isBlank()) {
            view.showMessage("Для CSV логирования нужно указать период и имя CSV файла\n");
            return;
        }

        DeviceCsvLogger logger = new DeviceCsvLogger(csvFile);
        List<DeviceSnapshot> previous = new ArrayList<>();
        view.showMessage("Запущено логирование в CSV. Для остановки нажмите Ctrl + C\n");

        while (true) {
            ProtocolTextMessage request = new ProtocolTextMessage();
            request.put("command", "SHOW_ONCE");
            ProtocolTextMessage response = sendRequest(request);
            if (response != null && isOk(response)) {
                List<DeviceSnapshot> current = parseSnapshots(response);
                try {
                    logger.appendChanges(previous, current);
                    previous = current;
                    view.showMessage("Изменения записаны в CSV: " + csvFile + "\n");
                } catch (IOException e) {
                    view.showMessage("Ошибка записи в CSV: " + e.getMessage() + "\n");
                }
            }
            sleep(period);
        }
    }

    private void handleAndPrint(ProtocolTextMessage request, boolean showDevices) {
        ProtocolTextMessage response = sendRequest(request);
        if (response == null) {
            return;
        }

        view.showMessage(response.get("message") + "\n");
        view.showStats(response.get("current_file"), response.get("objects_found"), response.get("properties_read"), response.get("properties_missing"));
        if (showDevices && isOk(response)) {
            view.showDevices(parseSnapshots(response));
        }
    }

    private ProtocolTextMessage sendRequest(ProtocolTextMessage request) {
        try {
            ProtocolTextMessage response = serverClient.send(request);
            if (!isOk(response)) {
                view.showMessage("Ошибка сервера: " + response.get("message") + "\n");
            }
            return response;
        } catch (IOException e) {
            view.showMessage("Ошибка сети: " + e.getMessage() + "\n");
            return null;
        }
    }

    private boolean isOk(ProtocolTextMessage response) {
        return "OK".equalsIgnoreCase(response.get("status"));
    }

    private List<DeviceSnapshot> parseSnapshots(ProtocolTextMessage response) {
        List<DeviceSnapshot> snapshots = new ArrayList<>();
        for (String deviceLine : response.getDeviceLines()) {
            snapshots.add(DeviceSnapshot.fromCompactLine(deviceLine));
        }
        return snapshots;
    }

    private Map<String, String> parseOptions(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("--")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    options.put(current, args[i + 1]);
                    i++;
                } else {
                    options.put(current, "true");
                }
            }
        }
        return options;
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
