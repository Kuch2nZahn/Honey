package io.github.thewebcode.networking;

import io.github.thewebcode.HoneyMod;
import io.github.thewebcode.honey.netty.HoneyClient;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HoneyClientManagingService {
    private HoneyClient honeyClient;
    private final PacketEventRegistry packetEventRegistry;
    private final IPacketRegistry packetRegistry;

    public HoneyClientManagingService() {
        this.packetRegistry = new HoneyPacketRegistry();
        this.packetEventRegistry = new PacketEventRegistry();
    }

    public void secureStart(InetSocketAddress toConnect) {
        if (this.honeyClient != null) shutdown();
        start(toConnect);
    }

    public void secureStartWithCallback(InetSocketAddress toConnect, Consumer<Future<? super Void>> callback) {
        if (this.honeyClient != null) shutdown();
        try {
            this.honeyClient = new HoneyClient(toConnect, this.packetRegistry, callback, packetEventRegistry);
        } catch (InterruptedException e) {
            System.out.println("Could not start local HoneyClient. Please Contact Developer with Error Code CEM1");
        }
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

    public HoneyClient getHoneyClient() {
        return honeyClient;
    }

    public static void send(HoneyPacket packet) {
        get().getHoneyClient().send(packet);
    }

    public static HoneyClientManagingService get() {
        return HoneyMod.getInstance().getHoneyClientManagingService();
    }
}
