package io.github.thewebcode.honey.netty.packet.impl.gui;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.util.Arrays;
import java.util.Collections;

public class HoneyGuiButtonToggledC2SPacket extends HoneyPacket {
    private String buttonID;
    private boolean newValue;

    public HoneyGuiButtonToggledC2SPacket() {
    }

    public boolean getNewValue() {
        return newValue;
    }

    public void setNewValue(boolean newValue) {
        this.newValue = newValue;
    }

    public String getButtonID() {
        return buttonID;
    }

    public void setButtonID(String buttonID) {
        this.buttonID = buttonID;
    }

    @Override
    public void read(PacketBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        buffer.readStringCollection().forEach(sb::append);
        this.buttonID = sb.toString();
        this.newValue = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(Collections.singletonList(buttonID));
        buffer.writeBoolean(newValue);
    }
}
