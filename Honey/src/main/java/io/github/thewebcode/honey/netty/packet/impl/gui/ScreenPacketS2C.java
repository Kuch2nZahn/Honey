package io.github.thewebcode.honey.netty.packet.impl.gui;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.util.Collections;
import java.util.List;

public class ScreenPacketS2C extends HoneyPacket {
    private String serializedScreen;

    public ScreenPacketS2C() {
    }

    public ScreenPacketS2C setScreen(String serializedScreen) {
        this.serializedScreen = serializedScreen;
        return this;
    }

    public String getScreen() {
        return serializedScreen;
    }

    @Override
    public void read(PacketBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> strings = buffer.readStringCollection();
        strings.forEach(stringBuilder::append);
        this.serializedScreen = stringBuilder.toString();
        System.out.println("serializedScreen = " + serializedScreen);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(Collections.singletonList(serializedScreen));
    }
}
