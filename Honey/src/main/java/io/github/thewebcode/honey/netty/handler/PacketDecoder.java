package io.github.thewebcode.honey.netty.handler;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private final IPacketRegistry packetRegistry;

    public PacketDecoder(IPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int packetId = byteBuf.readInt();
        if (!packetRegistry.containsPacketId(packetId)) throw new DecoderException("Received invalid packet id");

        long sessionId = byteBuf.readLong();
        PacketBuffer buffer = new PacketBuffer(byteBuf.readBytes(byteBuf.readableBytes()));
        HoneyPacket packet = packetRegistry.constructPacket(packetId);
        packet.setSessionId(sessionId);
        packet.read(buffer);

        list.add(packet);
    }


}
