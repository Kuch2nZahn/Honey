package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.netty.packet.impl.HandshakeC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.ScreenPacketS2C;

public class HoneyPacketRegistry extends SimplePacketRegistry {
    public HoneyPacketRegistry() {
        super();
        registerPacket(2, ScreenPacketS2C.class);
        registerPacket(1, HandshakeC2SPacket.class);
    }
}
