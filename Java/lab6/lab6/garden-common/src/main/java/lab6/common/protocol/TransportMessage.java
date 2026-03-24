package lab6.common.protocol;

public class TransportMessage {
    private final int requestId;
    private final byte[] payload;

    public TransportMessage(int requestId, byte[] payload) {
        this.requestId = requestId;
        this.payload = payload;
    }

    public int getRequestId() {
        return requestId;
    }

    public byte[] getPayload() {
        return payload;
    }
}
