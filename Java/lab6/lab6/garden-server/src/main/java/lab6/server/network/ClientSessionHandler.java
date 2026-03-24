package lab6.server.network;

import lab6.common.protocol.PacketIO;
import lab6.common.protocol.ProtocolTextMessage;
import lab6.common.protocol.TransportMessage;

import java.io.IOException;
import java.net.Socket;

public class ClientSessionHandler implements Runnable {
    private final Socket socket;
    private final RequestProcessor requestProcessor;

    public ClientSessionHandler(Socket socket, RequestProcessor requestProcessor) {
        this.socket = socket;
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void run() {
        try (Socket currentSocket = socket) {
            TransportMessage requestTransport = PacketIO.readMessage(currentSocket.getInputStream());
            ProtocolTextMessage request = ProtocolTextMessage.fromBytes(requestTransport.getPayload());
            ProtocolTextMessage response = requestProcessor.process(request);
            PacketIO.sendMessage(currentSocket.getOutputStream(), requestTransport.getRequestId(), response.toBytes());
        } catch (IOException e) {
            System.out.println("Ошибка обработки клиента: " + e.getMessage());
        }
    }
}
