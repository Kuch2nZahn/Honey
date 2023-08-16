package io.github.thewebcode.honey.netty.io;

import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.netty.channel.ChannelOutboundInvoker;

public interface Responder {
    void respond(HoneyPacket packet);

    static Responder forId(Long sessionId, ChannelOutboundInvoker channelOutboundInvoker) {
        return packet -> {
            packet.setSessionId(sessionId);
            channelOutboundInvoker.writeAndFlush(packet);
        };
    }

}
