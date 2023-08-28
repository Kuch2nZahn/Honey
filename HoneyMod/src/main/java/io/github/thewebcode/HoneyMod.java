package io.github.thewebcode;

import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyHelloC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.HoneyUpdateLanguageSettingC2SPacket;
import io.github.thewebcode.networking.HoneyClientManagingService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
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
            String language = MinecraftClient.getInstance().options.language;
            HoneyUpdateLanguageSettingC2SPacket languagePacket = new HoneyUpdateLanguageSettingC2SPacket();
            switch (language) {
                case "en_us" -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.EN);
                case "de_de" -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.DE);
                default -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.EN);
            }

            languagePacket.setReceiverUUID(HoneyUUID.SERVER);
            HoneyClientManagingService.sendPacket(languagePacket);

            HoneyHelloC2SPacket honeyHelloC2SPacket = new HoneyHelloC2SPacket();
            String name = MinecraftClient.getInstance().getSession().getProfile().getName();
            honeyHelloC2SPacket.setPlayerName(name);
            honeyHelloC2SPacket.setReceiverUUID(HoneyUUID.SERVER);
            HoneyClientManagingService.sendPacket(honeyHelloC2SPacket);
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