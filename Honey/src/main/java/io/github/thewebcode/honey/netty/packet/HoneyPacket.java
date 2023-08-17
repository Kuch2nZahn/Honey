package io.github.thewebcode.honey.netty.packet;

import io.github.thewebcode.honey.netty.io.Decoder;
import io.github.thewebcode.honey.netty.io.Encoder;
import io.github.thewebcode.honey.netty.io.HoneyUUID;

import java.util.concurrent.ThreadLocalRandom;

public abstract class HoneyPacket implements Encoder, Decoder {
    /**
     * Shows, where the packet came from
     * Server: 0000
     * Client: UUID of the Player
     */
    private String senderUUID;
    /**
     * Shows, where the packet should belong to
     * Server: 0000
     * All_players: 1111
     * Specific Player: Players UUID
     */
    private String receiverUUID;
    private long sessionId = ThreadLocalRandom.current().nextLong();

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public void setSenderUUID(HoneyUUID uuid) {
        this.senderUUID = uuid.getValue();
    }

    public void setReceiverUUID(String receiverUUID) {
        this.receiverUUID = receiverUUID;
    }

    public void setReceiverUUID(HoneyUUID uuid) {
        this.receiverUUID = uuid.getValue();
    }

    public String getReceiverUUID() {
        return receiverUUID;
    }
}
