package io.github.thewebcode;

import io.github.thewebcode.honey.netty.HoneyClient;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.event.PacketSubscriber;
import io.github.thewebcode.honey.netty.packet.impl.gui.ScreenPacketS2C;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.netty.channel.ChannelHandlerContext;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoneyMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("honeymod");
	private IPacketRegistry packetRegistry;
	private PacketEventRegistry packetEventRegistry;

	@Override
	public void onInitialize() {
		this.packetRegistry = new HoneyPacketRegistry();
		this.packetEventRegistry = new PacketEventRegistry();

		packetEventRegistry.registerEvents(new Object() {
			@PacketSubscriber
			public void onScreenReceive(ScreenPacketS2C packet, ChannelHandlerContext context) {
				System.out.println("Received Screen Packet");
			}
		});

		HoneyClient honeyClient = new HoneyClient(packetRegistry, (future) -> {
		}, packetEventRegistry);
		System.out.println("Honey Client Started");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> honeyClient.shutdown()));
	}
}