package io.github.thewebcode.honey.netty.event;

import io.github.thewebcode.honey.netty.io.Responder;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class PacketEventRegistry {
    private final String receiverUUID;

    public PacketEventRegistry(String receiverUUID) {
        this.receiverUUID = receiverUUID;
    }
    private final Set<RegisteredPacketSubscriber> subscribers = new HashSet<>();

    public void registerEvents(Object holder) {
        subscribers.add(new RegisteredPacketSubscriber(holder));
    }

    public void invoke(HoneyPacket packet, ChannelHandlerContext ctx) {
        if (!packet.getReceiverUUID().equals(this.receiverUUID)) return;
        try {
            for (RegisteredPacketSubscriber subscriber : subscribers) {
                subscriber.invoke(packet, ctx, Responder.forId(packet.getSessionId(), ctx));
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
