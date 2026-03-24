package lab5.controller;

import lab5.view.ConsoleView;

import java.util.HashMap;
import java.util.Map;

public class ApplicationController {
    private final ConsoleView view;
    private final DeviceController device_controller;

    public ApplicationController(){
        view = new ConsoleView();
        device_controller = new DeviceController(view);
    }

    public void run(String[] args){
        if (args == null || args.length == 0) {
            view.showHelp();
            return;
        }

        String command = args[0];
        Map<String, String> options = parseOptions(args);

        if (options.containsKey("--file")) {
            device_controller.setFileName(options.get("--file"));
        }

        switch (command) {
            case "--help":
            case "help":
                view.showHelp();
                break;
            case "--show-once":
                device_controller.showDevicesFromFileOnce();
                break;
            case "--replace-file":
                if (!options.containsKey("--new-file")) {
                    view.showMessage("Для команды --replace-file нужно указать --new-file <имя_файла>");
                    return;
                }
                device_controller.replaceFile(options.get("--new-file"));
                device_controller.showDevicesFromFileOnce();
                break;
            case "--watch":
                if (!options.containsKey("--period")) {
                    view.showMessage("Для команды --watch нужно указать --period <секунды>");
                    return;
                }
                device_controller.watchFile(parsePeriod(options.get("--period")));
                break;
            case "--csv-log":
                if (!options.containsKey("--period") || !options.containsKey("--csv")) {
                    view.showMessage("Для команды --csv-log нужно указать --period <секунды> и --csv <имя_csv>");
                    return;
                }
                device_controller.csvLogChanges(parsePeriod(options.get("--period")), options.get("--csv"));
                break;
            case "--interactive":
                device_controller.runInteractive();
                break;
            default:
                view.showMessage("Неизвестный ключ запуска\n");
                view.showHelp();
                break;
        }
    }

    private int parsePeriod(String value){
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e){
            return -1;
        }
    }

    private Map<String, String> parseOptions(String[] args){
        Map<String, String> options = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("--")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    options.put(current, args[i + 1]);
                    i++;
                }
                else {
                    options.put(current, "true");
                }
            }
        }
        return options;
    }
}
