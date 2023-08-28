package io.github.thewebcode.honey.event;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.event.impl.PlayerUpdateLanguageEvent;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.netty.packet.impl.HoneyUpdateLanguageSettingC2SPacket;
import io.github.thewebcode.honey.utils.MessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        ConfigManager configManager = Honey.getInstance().getConfigManager();
        boolean showHoneyServer = !configManager.getPluginConfig().isSet("show_honey_server") || configManager.getPluginConfig().getBoolean("show_honey_server");
        try {
            if (showHoneyServer) {
                event.setServerIcon(Bukkit.loadServerIcon(Honey.getInstance().getConfigManager().getServerIcon()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventTarget
    public void onPlayerLanguageUpdate(PlayerUpdateLanguageEvent event) {
        Player player = event.getPlayer();
        HoneyUpdateLanguageSettingC2SPacket.Language language = event.getLanguage();

        Honey.getInstance().getMessageBuilder().putPlayerLanguage(player, language);

        HoneyToastS2CPacket toastPacket = new HoneyToastS2CPacket();

        String languageUpdateToastTitle = MessageBuilder.getTranslatableMessage(player, "language_update_toast_title");
        String languageUpdateToastDescription = MessageBuilder.getTranslatableMessage(player, "language_update_toast_description");

        toastPacket.setType(HoneyToastS2CPacket.Type.PERIODIC_NOTIFICATION);
        toastPacket.setTitle(languageUpdateToastTitle);
        toastPacket.setDescription(String.format(languageUpdateToastDescription, language.getValue()));
        toastPacket.setReceiverUUID(player.getUniqueId().toString());
        toastPacket.setSenderUUID(HoneyUUID.SERVER);
        HoneyPacketServer.sendPacket(toastPacket);

    }
}
