package lab6.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GardenServer {
    private final String host;
    private final int port;
    private final RequestProcessor requestProcessor;

    public GardenServer(String host, int port, RequestProcessor requestProcessor) {
        this.host = host;
        this.port = port;
        this.requestProcessor = requestProcessor;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на " + host + ":" + port);
            System.out.println("Сетевой стек: TCP, собственный бинарный протокол, размер кадра до 1400 байт\n");
            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new ClientSessionHandler(socket, requestProcessor));
                thread.start();
            }
        }
    }
}
