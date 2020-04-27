package client;

import com.google.protobuf.Message;
import handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import packet.CRC16CheckSum;
import packet.Packet;

import java.net.URI;

public class GameClient {
    private static GameClient ins = null;
    private static Channel channel;

    private final CRC16CheckSum checkSum = new CRC16CheckSum();

    public static GameClient instance(){
        if(ins == null){
            ins = new GameClient();
        }
        return ins;
    }

    public void connect(String host, int port){
        EventLoopGroup client = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(client);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        bootstrap.handler(new GameClientInitializer());

        ChannelFuture future;
        try {
            URI websocketURI = new URI(String.format("ws://%s:%d/", host, port));
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            //进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String)null, true,httpHeaders);
            channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
            ClientHandler handler = (ClientHandler)channel.pipeline().get("client_handler");
            handler.setHandshaker(handshaker);
            // 通过它构造握手响应消息返回给客户端，
            // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
            // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            future = handler.handshakeFuture().sync();
            System.out.println("----channel:"+future.channel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //future.channel().closeFuture().awaitUninterruptibly();
    }

    public void send(Message msg) {
        if (channel == null || msg == null || !channel.isWritable()) {
            return;
        }
        int cmd = ProtoManager.getMessageID(msg);
        Packet packet = new Packet(Packet.HEAD_TCP, cmd, msg.toByteArray());
        channel.writeAndFlush(packet);
    }

    public void setChannel(Channel ch){
        channel = ch;
    }

    public Channel getChannel(){
        return channel;
    }
}
