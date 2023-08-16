package io.github.thewebcode.honey.netty.event;

import io.github.thewebcode.honey.netty.io.Responder;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class InvokableEventMethod {
    private final Object holder;
    private final Method method;
    private final Class<? extends HoneyPacket> packetClass;


    public InvokableEventMethod(Object holder, Method method, Class<? extends HoneyPacket> packetClass) {
        this.holder = holder;
        this.method = method;
        this.packetClass = packetClass;

        this.method.setAccessible(true);
    }

    public void invoke(HoneyPacket packet, ChannelHandlerContext ctx, Responder responder) throws InvocationTargetException, IllegalAccessException {
        if (!packetClass.equals(packet.getClass())) return;

        Object[] params = new Object[method.getParameterCount()];
        int index = 0;
        for (Parameter parameter : method.getParameters()) {
            if (HoneyPacket.class.isAssignableFrom(parameter.getType())) {
                params[index++] = packet;
                continue;
            }

            if (ChannelHandlerContext.class.isAssignableFrom(parameter.getType())) {
                params[index++] = ctx;
                continue;
            }

            if (Responder.class.isAssignableFrom(parameter.getType())) {
                params[index++] = responder;
            }
        }

        method.invoke(holder, params);
    }
}
