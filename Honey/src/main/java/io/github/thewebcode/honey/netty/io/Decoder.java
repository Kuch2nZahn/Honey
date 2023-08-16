package io.github.thewebcode.honey.netty.io;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;

public interface Decoder {
    void read(PacketBuffer buffer);
}
