package io.github.thewebcode.honey.netty.io;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;

public interface CallableDecoder<T> {
    T read(PacketBuffer buffer);
}
