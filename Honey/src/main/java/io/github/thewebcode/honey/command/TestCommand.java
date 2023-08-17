package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.utils.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HoneyPacketServer honeyPacketServer = Honey.getInstance().getHoneyPacketServer();
        HoneyToastS2CPacket toastPacket = new HoneyToastS2CPacket();
        String toastTitle = MessageBuilder.getMessage("honey_welcome_title");
        String toastDescription = MessageBuilder.getMessage("honey_welcome_de");
        toastPacket.setSenderUUID(HoneyUUID.SERVER);
        toastPacket.setReceiverUUID(HoneyUUID.ALL_PLAYERS);
        toastPacket.setTitle(toastTitle);
        toastPacket.setDescription(toastDescription);
        toastPacket.setType(HoneyToastS2CPacket.Type.UNSECURE_SERVER_WARNING);
        honeyPacketServer.send(toastPacket);
        return false;
    }
}
