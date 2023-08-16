package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.exception.PacketRegistrationException;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.lang.reflect.InvocationTargetException;

public interface IPacketRegistry {
    void registerPacket(int packetId, HoneyPacket packet) throws PacketRegistrationException;

    void registerPacket(int packetId, Class<? extends HoneyPacket> packet) throws PacketRegistrationException;

    int getPacketId(Class<? extends HoneyPacket> packetClass);

    <T extends HoneyPacket> T constructPacket(int packetId) throws InvocationTargetException, InstantiationException, IllegalAccessException;

    boolean containsPacketId(int id);
}
