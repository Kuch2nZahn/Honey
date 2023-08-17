package io.github.thewebcode.honey.netty.handler;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class PacketEncoder extends MessageToByteEncoder<HoneyPacket> {
    private final IPacketRegistry packetRegistry;

    public PacketEncoder(IPacketRegistry packetRegistry) {
        this.packetRegistry = packetRegistry;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HoneyPacket packet, ByteBuf byteBuf) throws Exception {
        int packetId = packetRegistry.getPacketId(packet.getClass());
        if (packetId < 0) throw new EncoderException("Returned PacketID by registry is < 0");

        byteBuf.writeInt(packetId);
        byteBuf.writeLong(packet.getSessionId());

        String senderUUID = packet.getSenderUUID();
        byte[] senderUUIDBytes = senderUUID.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(senderUUIDBytes.length);
        byteBuf.writeBytes(senderUUIDBytes);

        String receiverUUID = packet.getReceiverUUID();
        byte[] receiverUUIDBytes = receiverUUID.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(receiverUUIDBytes.length);
        byteBuf.writeBytes(receiverUUIDBytes);

        PacketBuffer buffer = new PacketBuffer();
        packet.write(buffer);

        byteBuf.writeBytes(buffer);
    }
}
