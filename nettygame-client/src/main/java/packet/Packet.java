package packet;

public class Packet {
    public static final byte HEAD_TCP = -128;
    public static final byte HEAD_UDP = 0;
    public static final byte HEAD_NEED_ACK = 64;
    public static final byte HEAD_ACK = 44;
    public static final byte HEAD_PROTOCOL_MASK = 3;
    public static final byte PROTOCOL_PROTOBUF = 0;
    public static final byte PROTOCOL_JSON = 1;
    private byte head;
    private short sid;
    private int cmd;
    private byte[] bytes;

    public Packet(byte head, int cmd, byte[] bytes) {
        this(head, (short)0, cmd, bytes);
    }

    public Packet(byte head, short sid, int cmd, byte[] bytes){
        this.cmd = cmd;
        this.bytes = bytes;
        this.head = head;
        this.sid = sid;
    }


    public byte getHead() {
        return head;
    }

    public short getSid() {
        return sid;
    }

    public int getCmd() {
        return cmd;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
