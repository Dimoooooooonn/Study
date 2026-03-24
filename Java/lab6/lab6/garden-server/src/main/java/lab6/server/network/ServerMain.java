package lab6.server.network;

import lab6.common.util.PropertiesLoader;
import lab6.server.service.DeviceService;

import java.util.Properties;

public class ServerMain {
    public static void main(String[] args) {
        Properties properties = PropertiesLoader.load("application.properties");
        String host = properties.getProperty("server.host", "127.0.0.1");
        int port = parseInt(properties.getProperty("server.port", "5050"), 5050);
        String fileName = properties.getProperty("server.storage.file", "devices.txt");

        DeviceService deviceService = new DeviceService(fileName);
        RequestProcessor requestProcessor = new RequestProcessor(deviceService);
        GardenServer server = new GardenServer(host, port, requestProcessor);

        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
