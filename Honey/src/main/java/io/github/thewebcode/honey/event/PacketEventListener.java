package io.github.thewebcode.honey.event;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.netty.event.PacketSubscriber;
import io.github.thewebcode.honey.netty.packet.impl.RequestServerConnectionC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.RequestServerConnectionS2CPacket;
import io.netty.channel.ChannelHandlerContext;

public class PacketEventListener {
    @PacketSubscriber
    public void onServerConnectionRequest(RequestServerConnectionC2SPacket packet, ChannelHandlerContext context) {
        RequestServerConnectionS2CPacket response = new RequestServerConnectionS2CPacket();
        response.setShouldJoin(true);
        response.setSessionId(packet.getSessionId());
        Honey.getInstance().getHoneyPacketServer().send(response);
    }
}
