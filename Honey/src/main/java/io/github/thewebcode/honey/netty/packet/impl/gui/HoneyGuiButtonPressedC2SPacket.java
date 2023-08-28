package io.github.thewebcode.honey.netty.packet.impl.gui;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.nio.charset.StandardCharsets;

public class HoneyGuiButtonPressedC2SPacket extends HoneyPacket {
    private String buttonID;

    public HoneyGuiButtonPressedC2SPacket() {
    }

    public String getButtonID() {
        return buttonID;
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    @Override
    public void read(PacketBuffer buffer) {
        int length = buffer.readInt();
        byte[] buttonIDBytes = new byte[length];
        buffer.readBytes(buttonIDBytes);
        this.buttonID = new String(buttonIDBytes, StandardCharsets.UTF_8);
    }

    @Override
    public void write(PacketBuffer buffer) {
        byte[] buttonIDBytes = buttonID.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(buttonIDBytes.length);
        buffer.writeBytes(buttonIDBytes);
    }
}
