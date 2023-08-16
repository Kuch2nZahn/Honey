package io.github.thewebcode.honey.netty.response;

import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PendingResponse<T extends HoneyPacket> {
    private final Long sent;
    private final Consumer<T> responseCallable;
    private final long timeout;

    public PendingResponse(Class<T> type, Consumer<T> responseCallable) {
        this(type, responseCallable, TimeUnit.SECONDS.toMillis(10));
    }

    public PendingResponse(Class<T> type, Consumer<T> responseCallable, long timeout) {
        this.timeout = timeout;
        this.sent = System.currentTimeMillis();
        this.responseCallable = responseCallable;
    }

    public void callResponseReceived(HoneyPacket packet) {
        responseCallable.accept((T) packet);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - sent > timeout;
    }
}
