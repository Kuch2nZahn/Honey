package io.github.thewebcode.honey.netty;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.handler.PacketChannelInboundHandler;
import io.github.thewebcode.honey.netty.handler.PacketDecoder;
import io.github.thewebcode.honey.netty.handler.PacketEncoder;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.github.thewebcode.honey.utils.MessageBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HoneyPacketServer extends ChannelInitializer<Channel> {
    private final ServerBootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final PacketEventRegistry packetEventRegistry;

    private final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel connectedChannel;

    public HoneyPacketServer(IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, PacketEventRegistry packetEventRegistry) {
        MessageBuilder.buildChatMessageAndAddToQueue("Starting Honey Server", MessageReceiver.CONSOLE);
        this.packetEventRegistry = packetEventRegistry;
        this.packetRegistry = packetRegistry;
        this.bootstrap = new ServerBootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, workerGroup)
                .childHandler(this)
                .channel(NioServerSocketChannel.class);

        try {
            this.bootstrap.bind(new InetSocketAddress("127.0.0.1", 2323))
                    .awaitUninterruptibly().sync().addListener(doneCallback::accept);

            MessageBuilder.buildChatMessageAndAddToQueue("Honey Server started", MessageReceiver.CONSOLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        channel.pipeline().addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(packetEventRegistry));
        this.connectedChannel = channel;
    }

    public void send(HoneyPacket packet) {
        if (this.connectedChannel == null) return;
        this.connectedChannel.writeAndFlush(packet);
    }

    public static void sendPacket(HoneyPacket packet) {
        Honey.getInstance().getHoneyPacketServer().send(packet);
    }

    public void shutdown() {
        try {
            MessageBuilder.buildChatMessageAndAddToQueue("Shuting down Honey Server...", MessageReceiver.CONSOLE);
            parentGroup.shutdownGracefully().get();
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
