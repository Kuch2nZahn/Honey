package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.netty.packet.impl.HandshakeC2SPacket;

public class HoneyPacketRegistry extends SimplePacketRegistry {
    public HoneyPacketRegistry() {
        super();
        registerPacket(1, HandshakeC2SPacket.class);
    }
}
