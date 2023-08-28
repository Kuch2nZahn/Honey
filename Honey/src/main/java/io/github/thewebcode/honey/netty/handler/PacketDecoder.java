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
    private int packetLength = -1;

    public PacketDecoder(IPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (packetLength == -1) {
            if (byteBuf.readableBytes() < 4) return;
            packetLength = byteBuf.readInt();
        }

        if (byteBuf.readableBytes() < packetLength) return;

        ByteBuf packetData = byteBuf.readSlice(packetLength);
        int packetId = packetData.readInt();

        if (!packetRegistry.containsPacketId(packetId))
            throw new DecoderException("Received invalid packet id: " + packetId);
        long sessionId = packetData.readLong();

        int senderUUIDLength = packetData.readInt();
        byte[] senderUUIDBytes = new byte[senderUUIDLength];
        packetData.readBytes(senderUUIDBytes);
        String senderUUID = new String(senderUUIDBytes, StandardCharsets.UTF_8);

        int receiverUUIDLength = packetData.readInt();
        byte[] receiverUUIDBytes = new byte[receiverUUIDLength];
        packetData.readBytes(receiverUUIDBytes);

        String receiverUUID = new String(receiverUUIDBytes, StandardCharsets.UTF_8);

        PacketBuffer buffer = new PacketBuffer(packetData.readBytes(packetData.readableBytes()));
        HoneyPacket packet = packetRegistry.constructPacket(packetId);
        packet.setSessionId(sessionId);
        packet.setSenderUUID(senderUUID);
        packet.setReceiverUUID(receiverUUID);

        //Ensure
        packet.read(buffer);
        packetLength = -1;

        list.add(packet);
    }
}
