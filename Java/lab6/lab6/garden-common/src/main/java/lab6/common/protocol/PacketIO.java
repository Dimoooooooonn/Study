package lab6.common.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class PacketIO {
    private PacketIO() {
    }

    public static void sendMessage(OutputStream outputStream, int requestId, byte[] payload) throws IOException {
        DataOutputStream out = new DataOutputStream(outputStream);
        if (payload == null) {
            payload = new byte[0];
        }

        int offset = 0;
        boolean first = true;

        if (payload.length == 0) {
            writeFrame(out, requestId, payload, 0, 0, true, true);
            out.flush();
            return;
        }

        while (offset < payload.length) {
            int chunkLength = Math.min(ProtocolConstants.MAX_CHUNK_SIZE, payload.length - offset);
            boolean last = offset + chunkLength >= payload.length;
            writeFrame(out, requestId, payload, offset, chunkLength, first, last);
            first = false;
            offset += chunkLength;
        }
        out.flush();
    }

    private static void writeFrame(DataOutputStream out, int requestId, byte[] payload, int offset, int chunkLength,
                                   boolean first, boolean last) throws IOException {
        byte flags = 0;
        if (first) {
            flags |= ProtocolConstants.FLAG_FIRST;
        }
        if (last) {
            flags |= ProtocolConstants.FLAG_LAST;
        }

        out.writeShort(ProtocolConstants.MAGIC);
        out.writeByte(ProtocolConstants.VERSION);
        out.writeByte(flags);
        out.writeInt(requestId);
        out.writeInt(payload.length);
        out.writeInt(chunkLength);
        if (chunkLength > 0) {
            out.write(payload, offset, chunkLength);
        }
    }

    public static TransportMessage readMessage(InputStream inputStream) throws IOException {
        DataInputStream in = new DataInputStream(inputStream);
        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        int requestId = -1;
        int expectedLength = -1;
        boolean lastFrame = false;

        while (!lastFrame) {
            short magic = in.readShort();
            if (magic != ProtocolConstants.MAGIC) {
                throw new IOException("Некорректная сигнатура сетевого пакета");
            }

            byte version = in.readByte();
            if (version != ProtocolConstants.VERSION) {
                throw new IOException("Некорректная версия протокола");
            }

            byte flags = in.readByte();
            int currentRequestId = in.readInt();
            int totalLength = in.readInt();
            int chunkLength = in.readInt();

            if (requestId == -1) {
                requestId = currentRequestId;
            }
            if (expectedLength == -1) {
                expectedLength = totalLength;
            }

            if (currentRequestId != requestId) {
                throw new IOException("В потоке обнаружены пакеты разных запросов");
            }
            if (chunkLength < 0 || chunkLength > ProtocolConstants.MAX_CHUNK_SIZE) {
                throw new IOException("Некорректный размер фрагмента пакета");
            }

            if (chunkLength > 0) {
                byte[] chunk = new byte[chunkLength];
                in.readFully(chunk);
                payload.write(chunk);
            }

            lastFrame = (flags & ProtocolConstants.FLAG_LAST) != 0;
        }

        byte[] payloadBytes = payload.toByteArray();
        if (expectedLength != -1 && payloadBytes.length != expectedLength) {
            payloadBytes = Arrays.copyOf(payloadBytes, payloadBytes.length);
        }
        return new TransportMessage(requestId, payloadBytes);
    }
}
