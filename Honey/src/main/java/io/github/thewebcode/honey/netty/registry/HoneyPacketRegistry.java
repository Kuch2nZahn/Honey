package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.netty.packet.impl.*;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyGuiButtonPressedC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyGuiButtonToggledC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyScreenS2CPacket;

public class HoneyPacketRegistry extends SimplePacketRegistry {
    public HoneyPacketRegistry() {
        super();
        registerPacket(1, HoneyHelloC2SPacket.class);
        registerPacket(2, RequestServerConnectionS2CPacket.class);
        registerPacket(3, RequestServerConnectionC2SPacket.class);
        registerPacket(4, HoneyToastS2CPacket.class);
        registerPacket(5, HoneyScreenS2CPacket.class);
        registerPacket(6, HoneyGuiButtonToggledC2SPacket.class);
        registerPacket(7, HoneyGuiButtonPressedC2SPacket.class);
        registerPacket(8, HoneyCloseScreenS2CPacket.class);
    }
}
