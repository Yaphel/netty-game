package nettygame.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import nettygame.handler.GameHandler;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        channel.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
        channel.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
        channel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
        channel.pipeline().addLast("handler",new GameHandler());//自定义的业务handler
    }
}
