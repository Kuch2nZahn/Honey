package io.github.thewebcode.honey.netty.response;

import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.netty.channel.ChannelOutboundInvoker;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RespondingPacket<T extends HoneyPacket> {
    private static final Long2ObjectMap<PendingResponse<?>> pendingResponses = new Long2ObjectArrayMap<>();
    private final HoneyPacket toSend;
    private final long timeout;
    private final Class<T> responseType;
    private final Consumer<T> callback;

    public RespondingPacket(String senderUUID, HoneyPacket toSend, long timeout, Class<T> responseType, Consumer<T> callback) {
        this.toSend = toSend;
        toSend.setSenderUUID(senderUUID);
        this.timeout = timeout;
        this.responseType = responseType;
        this.callback = callback;
    }

    public RespondingPacket(String senderUUID, HoneyPacket toSend, Class<T> responseType, Consumer<T> callback) {
        this(senderUUID, toSend, TimeUnit.SECONDS.toMillis(10), responseType, callback);
    }

    public void send(ChannelOutboundInvoker invoker) {
        invoker.writeAndFlush(toSend);
        pendingResponses.put(toSend.getSessionId(), new PendingResponse<>(responseType, callback, timeout));
    }

    public static void callReceive(HoneyPacket packet) {
        if (!pendingResponses.containsKey(packet.getSessionId())) return;
        pendingResponses.get(packet.getSessionId()).callResponseReceived(packet);
        pendingResponses.remove(packet.getSessionId());

        cleanupPendingResponses();
    }

    private static void cleanupPendingResponses() {
        pendingResponses.forEach((sessionId, pendingResponse) -> {
            if (!pendingResponse.isExpired()) return;
            RespondingPacket.pendingResponses.remove(sessionId.longValue());
        });
    }
}
