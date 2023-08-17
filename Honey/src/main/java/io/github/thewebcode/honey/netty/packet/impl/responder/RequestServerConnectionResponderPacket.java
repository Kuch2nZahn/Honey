package io.github.thewebcode.honey.netty.packet.impl.responder;

import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.packet.impl.RequestServerConnectionS2CPacket;
import io.github.thewebcode.honey.netty.response.RespondingPacket;

import java.util.function.Consumer;

public class RequestServerConnectionResponderPacket extends RespondingPacket<RequestServerConnectionS2CPacket> {
    public RequestServerConnectionResponderPacket(HoneyPacket toSend, Class<RequestServerConnectionS2CPacket> responseType, Consumer<RequestServerConnectionS2CPacket> callback) {
        super(toSend, responseType, callback);
    }
}
