package io.github.thewebcode.honey.netty.packet.impl.gui;

import io.github.thewebcode.honey.gui.HoneyGuiScreen;
import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.utils.ObjectHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScreenPacketS2C<T extends HoneyGuiScreen> extends HoneyPacket {
    private final T screen;

    public ScreenPacketS2C(T screen) {
        this.screen = screen;
    }

    public T getScreen() {
        return screen;
    }

    @Override
    public void read(PacketBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> strings = buffer.readStringCollection();
        strings.forEach(stringBuilder::append);
        String jsonString = stringBuilder.toString();
        System.out.println("jsonString = " + jsonString);
        HoneyGuiScreen honeyGuiScreen = ObjectHelper.gson.fromJson(jsonString, HoneyGuiScreen.class);
        System.out.println(honeyGuiScreen == null);
    }

    @Override
    public void write(PacketBuffer buffer) {
        String json = ObjectHelper.gson.toJson(screen);
        System.out.println("json = " + json);
        buffer.writeStringCollection(Collections.singletonList(json));
    }
}
