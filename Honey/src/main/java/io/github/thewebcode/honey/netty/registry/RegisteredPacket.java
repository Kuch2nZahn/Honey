package io.github.thewebcode.honey.netty.registry;

import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegisteredPacket {
    private final Class<? extends HoneyPacket> packetClass;
    private final Constructor<? extends HoneyPacket> constructor;

    public RegisteredPacket(Class<? extends HoneyPacket> packetClass) throws NoSuchMethodException {
        this.packetClass = packetClass;

        List<Constructor<?>> emptyConstructorList = Arrays.stream(packetClass.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0).collect(Collectors.toList());
        if (emptyConstructorList.size() == 0) {
            throw new NoSuchMethodException("Packet is missing no-args-constructor");
        }

        this.constructor = (Constructor<? extends HoneyPacket>) emptyConstructorList.get(0);
    }

    public Class<? extends HoneyPacket> getPacketClass() {
        return packetClass;
    }

    public Constructor<? extends HoneyPacket> getConstructor() {
        return constructor;
    }
}
