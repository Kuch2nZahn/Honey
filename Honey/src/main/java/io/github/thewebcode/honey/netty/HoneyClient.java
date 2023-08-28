package io.github.thewebcode.honey.netty;

import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.handler.PacketChannelInboundHandler;
import io.github.thewebcode.honey.netty.handler.PacketDecoder;
import io.github.thewebcode.honey.netty.handler.PacketEncoder;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HoneyClient extends ChannelInitializer<Channel> {
    private final Bootstrap bootstrap;
    private final IPacketRegistry packetRegistry;
    private final PacketEventRegistry eventRegistry;

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel connectedChannel;

    public HoneyClient(InetSocketAddress toConnect, IPacketRegistry packetRegistry, Consumer<Future<? super Void>> doneCallback, PacketEventRegistry eventRegistry) throws InterruptedException {
        this.packetRegistry = packetRegistry;
        this.eventRegistry = eventRegistry;
        this.bootstrap = new Bootstrap()
                .option(ChannelOption.AUTO_READ, true)
                .group(workerGroup)
                .handler(this)
                .channel(NioSocketChannel.class);

        this.bootstrap.connect(toConnect)
                .awaitUninterruptibly().sync().addListener(doneCallback::accept);
    }

    @Override
    protected void initChannel(@NotNull Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new PacketDecoder(packetRegistry), new PacketEncoder(packetRegistry), new PacketChannelInboundHandler(eventRegistry));
        this.connectedChannel = channel;
    }

    public void send(HoneyPacket packet) {
        if (this.connectedChannel == null) return;
        this.connectedChannel.writeAndFlush(packet);
    }

    public void shutdown() {
        try {
            workerGroup.shutdownGracefully().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Channel getConnectedChannel() {
        return connectedChannel;
    }
}
