package nettygame.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class Encoder extends ChannelOutboundHandlerAdapter {

    private final int limit;

    public Encoder(int limit) {
        this.limit = limit;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            int size = 7 + packet.getBytes().length;
            ByteBuf buf = ctx.alloc().buffer(size);
            try {
                buf.writeByte(packet.getHead());
                buf.writeShort(packet.getBytes().length + 4);
                buf.writeInt(packet.getCmd());
                buf.writeBytes(packet.getBytes());
                BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
                ctx.writeAndFlush(frame);
                return;
            } catch (Exception e) {
                buf.release();
            }
        }
        ctx.writeAndFlush(msg);
    }
}