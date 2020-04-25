package nettygame.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Packet> {
    private int limit;

    public Encoder(int limit){
        this.limit = limit;
    }

    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception{
        buf.writeByte(packet.getHead());
        buf.writeShort(packet.getBytes().length + 4);
        buf.writeInt(packet.getCmd());
        buf.writeBytes(packet.getBytes());
    }
}
