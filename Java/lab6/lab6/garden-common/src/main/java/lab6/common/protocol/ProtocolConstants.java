package lab6.common.protocol;

public final class ProtocolConstants {
    public static final short MAGIC = (short) 0x4744;
    public static final byte VERSION = 1;
    public static final int HEADER_SIZE = 16;
    public static final int MAX_FRAME_SIZE = 1400;
    public static final int MAX_CHUNK_SIZE = MAX_FRAME_SIZE - HEADER_SIZE;

    public static final byte FLAG_FIRST = 0x01;
    public static final byte FLAG_LAST = 0x02;

    private ProtocolConstants() {
    }
}
