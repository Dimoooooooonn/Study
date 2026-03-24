package lab6.client.controller;

import lab6.client.network.ServerClient;
import lab6.client.view.ConsoleView;
import lab6.common.util.PropertiesLoader;

import java.util.Properties;

public class ClientMain {
    public static void main(String[] args) {
        Properties properties = PropertiesLoader.load("application.properties");
        String host = properties.getProperty("server.host", "127.0.0.1");
        int port = parseInt(properties.getProperty("server.port", "5050"), 5050);

        ConsoleView view = new ConsoleView();
        ServerClient serverClient = new ServerClient(host, port);
        ClientController clientController = new ClientController(view, serverClient);
        clientController.run(args);
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
