package io.github.thewebcode.mixin;

import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyUpdateLanguageSettingC2SPacket;
import io.github.thewebcode.networking.HoneyClientManagingService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(method = "reload", at = @At("RETURN"))
    public void onReload(ResourceManager manager, CallbackInfo ci) {
        String language = MinecraftClient.getInstance().options.language;
        HoneyUpdateLanguageSettingC2SPacket languagePacket = new HoneyUpdateLanguageSettingC2SPacket();
        switch (language) {
            case "en_us" -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.EN);
            case "de_de" -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.DE);
            default -> languagePacket.setLanguage(HoneyUpdateLanguageSettingC2SPacket.Language.EN);
        }

        languagePacket.setReceiverUUID(HoneyUUID.SERVER);
        HoneyClientManagingService.sendPacket(languagePacket);
    }
}
