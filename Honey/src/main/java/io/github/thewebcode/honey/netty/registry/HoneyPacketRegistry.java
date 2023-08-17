package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.netty.packet.impl.HandshakeC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.RequestServerConnectionC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.RequestServerConnectionS2CPacket;

public class HoneyPacketRegistry extends SimplePacketRegistry {
    public HoneyPacketRegistry() {
        super();
        //even numbers = s2c
        //odd numbers = c2s
        registerPacket(1, HandshakeC2SPacket.class);
        registerPacket(2, RequestServerConnectionS2CPacket.class);
        registerPacket(3, RequestServerConnectionC2SPacket.class);
    }
}
