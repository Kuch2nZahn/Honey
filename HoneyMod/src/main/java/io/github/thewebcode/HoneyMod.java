package io.github.thewebcode;

import io.github.thewebcode.networking.HoneyClientManagingService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HoneyMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("honeymod");
    private static HoneyMod instance;
    private HoneyClientManagingService honeyClientManagingService;

	@Override
	public void onInitialize() {
        instance = this;
        this.honeyClientManagingService = new HoneyClientManagingService();

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {

		});

        Runtime.getRuntime().addShutdownHook(new Thread(() -> honeyClientManagingService.shutdown()));
    }

    public HoneyClientManagingService getHoneyClientManagingService() {
        return honeyClientManagingService;
    }

    public static HoneyMod getInstance() {
        return instance;
	}
}