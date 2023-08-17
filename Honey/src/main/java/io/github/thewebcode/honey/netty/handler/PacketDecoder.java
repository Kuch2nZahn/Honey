package io.github.thewebcode.honey.netty.handler;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.StandardCharsets;
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

        int senderUUIDLength = byteBuf.readInt();
        byte[] senderUUIDBytes = new byte[senderUUIDLength];
        byteBuf.readBytes(senderUUIDBytes);

        int receiverUUIDLength = byteBuf.readInt();
        byte[] receiverUUIDBytes = new byte[receiverUUIDLength];
        byteBuf.readBytes(receiverUUIDBytes);

        String senderUUID = new String(senderUUIDBytes, StandardCharsets.UTF_8);
        String receiverUUID = new String(receiverUUIDBytes, StandardCharsets.UTF_8);

        PacketBuffer buffer = new PacketBuffer(byteBuf.readBytes(byteBuf.readableBytes()));
        HoneyPacket packet = packetRegistry.constructPacket(packetId);
        packet.setSessionId(sessionId);
        packet.setSenderUUID(senderUUID);
        packet.setReceiverUUID(receiverUUID);
        packet.read(buffer);

        list.add(packet);
    }


}
