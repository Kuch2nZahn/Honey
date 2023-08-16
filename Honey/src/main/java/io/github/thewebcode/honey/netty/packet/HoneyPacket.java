package io.github.thewebcode.honey.netty.packet;

import io.github.thewebcode.honey.netty.io.Decoder;
import io.github.thewebcode.honey.netty.io.Encoder;

import java.util.concurrent.ThreadLocalRandom;

public abstract class HoneyPacket implements Encoder, Decoder {
    private long sessionId = ThreadLocalRandom.current().nextLong();

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }
}
