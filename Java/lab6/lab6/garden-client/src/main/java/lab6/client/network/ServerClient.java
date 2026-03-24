package lab6.client.network;

import lab6.common.protocol.PacketIO;
import lab6.common.protocol.ProtocolTextMessage;
import lab6.common.protocol.TransportMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerClient {
    private final String serverHost;
    private final int serverPort;
    private final AtomicInteger requestId = new AtomicInteger(1);

    public ServerClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public ProtocolTextMessage send(ProtocolTextMessage request) throws IOException {
        try (Socket socket = new Socket(serverHost, serverPort)) {
            int currentRequestId = requestId.getAndIncrement();
            PacketIO.sendMessage(socket.getOutputStream(), currentRequestId, request.toBytes());
            TransportMessage responseTransport = PacketIO.readMessage(socket.getInputStream());
            return ProtocolTextMessage.fromBytes(responseTransport.getPayload());
        }
    }
}
