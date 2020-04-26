package nettygame.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import nettygame.handler.GameHandler;
import nettygame.packet.CRC16CheckSum;
import nettygame.packet.Decoder;
import nettygame.packet.Encoder;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

    private final int upLimit = 2048;//解码大小限制
    private final int downLimit = 5120;//编码大小限制
    private final CRC16CheckSum upCheckSum = new CRC16CheckSum();//循环冗余校验

    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast("http-codec", new HttpServerCodec())//HTTP编解码器
                .addLast("aggregator", new HttpObjectAggregator(65536))//HTTP消息聚合
                .addLast("websocket", new WebSocketServerProtocolHandler("/", null, true))//处理http升级websocket，还有心跳
                .addLast("decoder", new Decoder(upCheckSum, upLimit))
                .addLast("server-handler", new GameHandler())
                .addLast("encoder", new Encoder(downLimit));
    }
}
