package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyScreenS2CPacket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TestScreen screen = new TestScreen();

        HoneyScreenS2CPacket packet = new HoneyScreenS2CPacket();
        packet.setScreen(screen);
        packet.setReceiverUUID(HoneyUUID.ALL_PLAYERS);
        packet.setSenderUUID(HoneyUUID.SERVER);

        HoneyPacketServer.sendPacket(packet);

        HoneyToastS2CPacket toastS2CPacket = new HoneyToastS2CPacket();
        toastS2CPacket.setReceiverUUID(HoneyUUID.ALL_PLAYERS);
        toastS2CPacket.setSenderUUID(HoneyUUID.SERVER);
        toastS2CPacket.setType(HoneyToastS2CPacket.Type.PERIODIC_NOTIFICATION);
        toastS2CPacket.setTitle("Packet send");
        toastS2CPacket.setDescription("This packet was sent!");

        HoneyPacketServer.sendPacket(toastS2CPacket);

        return false;
    }
}
