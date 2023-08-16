package io.github.thewebcode.honey.netty.io;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;

public interface CallableEncoder<T> {
    void write(T data, PacketBuffer buffer);
}
