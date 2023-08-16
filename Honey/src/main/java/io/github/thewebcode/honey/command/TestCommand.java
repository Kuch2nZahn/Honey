package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.message.Message;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.netty.packet.impl.HandshakeC2SPacket;
import io.github.thewebcode.honey.utils.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        MessageBuilder.buildChatMessageAndAddToQueue("Sending Test packet", MessageReceiver.CONSOLE, Message.Priority.HIGH);
        HandshakeC2SPacket packet = new HandshakeC2SPacket();
        Honey.getInstance().getHoneyPacketServer().send(packet);
        return false;
    }
}
