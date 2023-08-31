package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyScreenS2CPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HoneyToastS2CPacket packet1 = new HoneyToastS2CPacket();
        packet1.setTitle("One Player");
        packet1.setType(HoneyToastS2CPacket.Type.UNSECURE_SERVER_WARNING);
        packet1.setDescription("This is sent with UUID");
        packet1.setReceiverUUID(((Player) sender).getUniqueId().toString());
        packet1.setSenderUUID(HoneyUUID.SERVER);
        HoneyPacketServer.sendPacket(packet1);

        HoneyToastS2CPacket packet2 = new HoneyToastS2CPacket();
        packet2.setTitle("ALL_PLAYERS");
        packet2.setType(HoneyToastS2CPacket.Type.UNSECURE_SERVER_WARNING);
        packet2.setDescription("This is sent with ALL_PLAYERS");
        packet2.setReceiverUUID(HoneyUUID.ALL_PLAYERS);
        packet2.setSenderUUID(HoneyUUID.SERVER);
        HoneyPacketServer.sendPacket(packet2);

        TestScreen screen = new TestScreen();
        HoneyScreenS2CPacket screenPacket = new HoneyScreenS2CPacket();
        screenPacket.setScreen(screen);
        screenPacket.setReceiverUUID(((Player) sender).getUniqueId().toString());
        screenPacket.setSenderUUID(HoneyUUID.SERVER);
        Honey.sendPacket(screenPacket);

        return false;
    }
}
