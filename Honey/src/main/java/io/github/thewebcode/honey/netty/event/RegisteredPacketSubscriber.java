package io.github.thewebcode.honey.netty.event;

import io.github.thewebcode.honey.netty.io.Responder;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegisteredPacketSubscriber {
    private final Map<Class<? extends HoneyPacket>, Set<InvokableEventMethod>> handler = new HashMap<>();

    public RegisteredPacketSubscriber(Object subscriberClass) {
        for (Method method : subscriberClass.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(PacketSubscriber.class)) {
                continue;
            }
            Class<? extends HoneyPacket> packetClass = null;
            for (Parameter parameter : method.getParameters()) {
                if (HoneyPacket.class.isAssignableFrom(parameter.getType())) {
                    packetClass = (Class<? extends HoneyPacket>) parameter.getType();
                    continue;
                }
                if (ChannelHandlerContext.class.isAssignableFrom(parameter.getType())) {
                    continue;
                }
                if (Responder.class.isAssignableFrom(parameter.getType())) {
                    continue;
                }
                throw new IllegalArgumentException("Invalid parameter for @PacketSubscriber: " + parameter.getType().getSimpleName());
            }
            if (packetClass == null) {
                throw new IllegalArgumentException("Missing packet parameter for @PacketSubscriber");
            }
            handler.computeIfAbsent(packetClass, aClass -> new HashSet<>()).add(new InvokableEventMethod(
                    subscriberClass, method, packetClass
            ));
        }
    }

    public void invoke(HoneyPacket rawPacket, ChannelHandlerContext ctx, Responder responder) throws InvocationTargetException, IllegalAccessException {
        Set<InvokableEventMethod> methods = handler.get(rawPacket.getClass());
        if (methods == null) {
            return;
        }
        for (InvokableEventMethod method : methods) {
            method.invoke(rawPacket, ctx, responder);
        }
    }
}
