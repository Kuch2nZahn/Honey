package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HoneyToastS2CPacket toastPacket = new HoneyToastS2CPacket();
        toastPacket.setTitle("Updated Language Settings");
        toastPacket.setType(HoneyToastS2CPacket.Type.UNSECURE_SERVER_WARNING);
        toastPacket.setDescription(String.format("Your Language is now set to: %s", "TEST"));
        toastPacket.setReceiverUUID(((Player) sender).getUniqueId().toString());
        toastPacket.setSenderUUID(HoneyUUID.SERVER);
        HoneyPacketServer.sendPacket(toastPacket);

        return false;
    }
}
