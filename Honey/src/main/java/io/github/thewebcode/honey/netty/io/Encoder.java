package io.github.thewebcode.honey.netty.io;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;

public interface Encoder {
    void write(PacketBuffer buffer);
}
