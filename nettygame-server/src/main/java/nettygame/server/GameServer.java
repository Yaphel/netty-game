package nettygame.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nettygame.handler.GameHandler;
import nettygame.packet.CRC16CheckSum;
import nettygame.packet.Decoder;
import nettygame.packet.Encoder;

import java.net.InetSocketAddress;

public class GameServer {
    private  EventLoopGroup bossGroup;//监听SeverChannel
    private  EventLoopGroup workerGroup;//创建所有客户端Channel
    private  ServerBootstrap bootstrap;//netty服务端启动类

    public GameServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup(4);
        bootstrap = new ServerBootstrap();//netty服务端启动类，与客户端不同
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)//绑定服务端通道，与客户端不同
                .option(ChannelOption.SO_BACKLOG, 5)//指定客户端连接请求队列大小
                .childOption(ChannelOption.TCP_NODELAY, true);//关闭nagle算法，实时性高的游戏不需延迟粘包
    }

    public void bind(int port) {
        bootstrap.childHandler(new GameServerInitializer());
        InetSocketAddress address = new InetSocketAddress(port);
        try {
            bootstrap.bind(address).sync();//监听端口
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
