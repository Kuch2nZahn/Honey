package io.github.thewebcode.networking;

import io.github.thewebcode.HoneyMod;
import io.github.thewebcode.honey.netty.HoneyClient;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.event.PacketSubscriber;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.text.Text;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HoneyClientManagingService {
    private HoneyClient honeyClient;
    private final PacketEventRegistry packetEventRegistry;
    private final IPacketRegistry packetRegistry;

    public HoneyClientManagingService() {
        this.packetRegistry = new HoneyPacketRegistry();
        this.packetEventRegistry = new PacketEventRegistry(MinecraftClient.getInstance().getSession().getProfile().getId().toString());

        this.packetEventRegistry.registerEvents(new Object() {
            @PacketSubscriber
            public void onToastPacketReceive(HoneyToastS2CPacket packet, ChannelHandlerContext context) {
                System.out.println("packet.getSenderUUID() = " + packet.getSenderUUID());

                HoneyToastS2CPacket.Type rawType = packet.getType();
                SystemToast.Type type = SystemToast.Type.valueOf(rawType.toString());
                Text title = Text.literal(packet.getTitle());
                Text description = Text.literal(packet.getDescription());
                Toast toast = new SystemToast(type, title, description);
                MinecraftClient.getInstance().getToastManager().add(toast);
            }
        });
    }

    public void secureStart(InetSocketAddress toConnect) {
        if (this.honeyClient != null) shutdown();
        start(toConnect);
    }

    public void secureStartWithCallback(InetSocketAddress toConnect, Consumer<Future<? super Void>> callback) throws InterruptedException {
        if (this.honeyClient != null) shutdown();
        this.honeyClient = new HoneyClient(toConnect, this.packetRegistry, callback, packetEventRegistry);
    }

    public void start(InetSocketAddress toConnect) {
        try {
            this.honeyClient = new HoneyClient(toConnect, this.packetRegistry, (future) -> {
            }, packetEventRegistry);
        } catch (InterruptedException e) {
            System.out.println("Could not start local HoneyClient. Please Contact Developer with Error Code CEM1");
        }
    }

    public void shutdown() {
        if (this.honeyClient == null) return;

        honeyClient.shutdown();
        this.honeyClient = null;
    }

    public void send(HoneyPacket packet) {
        if (honeyClient == null) return;
        UUID id = MinecraftClient.getInstance().getSession().getProfile().getId();
        System.out.println("id = " + id);
        packet.setSenderUUID(id.toString());
        honeyClient.send(packet);
    }

    public static void sendPacket(HoneyPacket packet) {
        get().send(packet);
    }

    public HoneyClient getHoneyClient() {
        return honeyClient;
    }

    public static HoneyClientManagingService get() {
        return HoneyMod.getInstance().getHoneyClientManagingService();
    }
}
