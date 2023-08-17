package io.github.thewebcode.honey.netty.packet.impl;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

public class RequestServerConnectionS2CPacket extends HoneyPacket {
    private boolean shouldJoin;

    public RequestServerConnectionS2CPacket() {
    }

    public void setShouldJoin(boolean shouldJoin) {
        this.shouldJoin = shouldJoin;
    }

    public boolean shouldJoin() {
        return shouldJoin;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.shouldJoin = buffer.readBoolean();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(shouldJoin);
    }
}
