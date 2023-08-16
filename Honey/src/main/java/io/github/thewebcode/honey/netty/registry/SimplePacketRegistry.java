package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.exception.PacketRegistrationException;
import io.github.thewebcode.honey.message.Message;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.utils.MessageBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.lang.reflect.InvocationTargetException;

public class SimplePacketRegistry implements IPacketRegistry {
    private final Int2ObjectOpenHashMap<RegisteredPacket> packets = new Int2ObjectOpenHashMap<>();

    @Override
    public void registerPacket(int packetId, HoneyPacket packet) throws PacketRegistrationException {
        registerPacket(packetId, packet.getClass());
    }

    @Override
    public void registerPacket(int packetId, Class<? extends HoneyPacket> packet) {
        try {
            if (containsPacketId(packetId)) {
                throw new PacketRegistrationException("PacketID is already in use!");
            }
        } catch (PacketRegistrationException e) {
            e.printStackTrace();
        }

        try {
            RegisteredPacket registeredPacket = new RegisteredPacket(packet);
            this.packets.put(packetId, registeredPacket);
            MessageBuilder.buildChatMessageAndAddToQueue(String.format("Registered Packet from class: %s with Packet ID: %s", registeredPacket.getPacketClass().getSimpleName(), packetId), MessageReceiver.CONSOLE, Message.Priority.HIGH);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPacketId(Class<? extends HoneyPacket> packetClass) {
        for (ObjectIterator<Int2ObjectMap.Entry<RegisteredPacket>> it = packets.int2ObjectEntrySet().fastIterator(); it.hasNext(); ) {
            Int2ObjectMap.Entry<RegisteredPacket> entry = it.next();
            if (entry.getValue().getPacketClass().equals(packetClass)) {
                return entry.getIntKey();
            }
        }

        return -1;
    }

    @Override
    public <T extends HoneyPacket> T constructPacket(int packetId) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) packets.get(packetId).getConstructor().newInstance();
    }

    @Override
    public boolean containsPacketId(int id) {
        return packets.containsKey(id);
    }
}
