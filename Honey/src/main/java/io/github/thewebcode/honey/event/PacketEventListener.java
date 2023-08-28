package io.github.thewebcode.honey.event;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.event.PacketSubscriber;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.*;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyGuiButtonPressedC2SPacket;
import io.github.thewebcode.honey.utils.MessageBuilder;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PacketEventListener {
    @PacketSubscriber
    public void onServerConnectionRequest(RequestServerConnectionC2SPacket packet, ChannelHandlerContext context) {
        RequestServerConnectionS2CPacket response = new RequestServerConnectionS2CPacket();
        response.setSenderUUID(HoneyUUID.SERVER);
        response.setReceiverUUID(packet.getSenderUUID());
        response.setShouldJoin(true);
        response.setSessionId(packet.getSessionId());
        Honey.getInstance().getHoneyPacketServer().send(response);
    }

    @PacketSubscriber
    public void onReceiveButtonPressed(HoneyGuiButtonPressedC2SPacket packet, ChannelHandlerContext context) {
        String buttonID = packet.getButtonID();

        if (buttonID.equalsIgnoreCase("gui_button_close")) {
            UUID uuid = UUID.fromString(packet.getSenderUUID());
            Player player = Bukkit.getPlayer(uuid);

            HoneyCloseScreenS2CPacket honeyCloseScreenS2CPacket = new HoneyCloseScreenS2CPacket();
            honeyCloseScreenS2CPacket.setReceiverUUID(packet.getSenderUUID());
            honeyCloseScreenS2CPacket.setSenderUUID(HoneyUUID.SERVER);
            HoneyPacketServer.sendPacket(honeyCloseScreenS2CPacket);
        }
    }
    @PacketSubscriber
    public void onReceiveHello(HoneyHelloC2SPacket packet, ChannelHandlerContext context) {
        String playerName = packet.getPlayerName();
        ConfigManager configManager = Honey.getInstance().getConfigManager();
        YamlConfiguration pluginConfig = configManager.getPluginConfig();
        boolean logJoin = !pluginConfig.isSet("log_honey_join_in_console") || pluginConfig.getBoolean("log_honey_join_in_console");
        boolean sendWelcomeToast = !pluginConfig.isSet("send_honey_welcome_toast") || pluginConfig.getBoolean("send_honey_welcome_toast");

        if (logJoin)
            MessageBuilder.buildChatMessageAndAddToQueue(String.format("%s (%s) joined the Server with the Honey Mod!", playerName, packet.getSenderUUID()), MessageReceiver.CONSOLE);

        if (sendWelcomeToast) {
            HoneyToastS2CPacket welcomeToast = new HoneyToastS2CPacket();
            String toastTitle = MessageBuilder.getMessage("honey_welcome_title");
            String toastDescription = MessageBuilder.getMessage("honey_welcome_description");
            welcomeToast.setReceiverUUID(packet.getSenderUUID());
            welcomeToast.setSenderUUID(HoneyUUID.SERVER);
            welcomeToast.setType(HoneyToastS2CPacket.Type.TUTORIAL_HINT);
            welcomeToast.setTitle(toastTitle);
            welcomeToast.setDescription(toastDescription);
            HoneyPacketServer.sendPacket(welcomeToast);
        }
    }
}
