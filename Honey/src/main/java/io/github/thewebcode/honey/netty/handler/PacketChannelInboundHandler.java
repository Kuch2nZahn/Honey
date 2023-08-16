package io.github.thewebcode.honey.netty.handler;

import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.response.RespondingPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PacketChannelInboundHandler extends SimpleChannelInboundHandler<HoneyPacket> {
    private final PacketEventRegistry eventRegistry;

    public PacketChannelInboundHandler(PacketEventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HoneyPacket packet) throws Exception {
        RespondingPacket.callReceive(packet);
        eventRegistry.invoke(packet, channelHandlerContext);
    }
}
