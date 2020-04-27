package client;

import handler.GameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import packet.CRC16CheckSum;
import packet.Decoder;
import packet.Encoder;

public class GameClientInitializer extends ChannelInitializer<Channel> {
    private final CRC16CheckSum checkSum = new CRC16CheckSum();
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //HTTP编解码器
        pipeline.addLast("http_codec", new HttpClientCodec());
        //HTTP消息聚合,使用FullHttpResponse和FullHttpRequest到ChannelPipeline中的下一个ChannelHandler，这就消除了断裂消息，保证了消息的完整。
        pipeline.addLast("http_aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("protobuf_decoder", new Decoder(null, 5120));
        pipeline.addLast("client_handler", new GameHandler());
        pipeline.addLast("protobuf_encoder", new Encoder(checkSum, 2048));
    }
}
