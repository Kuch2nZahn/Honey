package io.github.thewebcode.honey.netty.packet.impl;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.util.Arrays;
import java.util.Collections;

public class HoneyHelloC2SPacket extends HoneyPacket {
    private String playerName;

    public HoneyHelloC2SPacket() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void read(PacketBuffer buffer) {
        StringBuilder stringBuilder = new StringBuilder();
        buffer.readStringCollection().forEach(stringBuilder::append);
        this.playerName = stringBuilder.toString();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(Collections.singletonList(playerName));
    }
}
