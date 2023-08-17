package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.gui.ExampleScreen;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.packet.impl.gui.ScreenPacketS2C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HoneyPacketServer honeyPacketServer = Honey.getInstance().getHoneyPacketServer();
        ExampleScreen exampleScreen = new ExampleScreen(null);
        ScreenPacketS2C<ExampleScreen> exampleScreenScreenPacketS2C = new ScreenPacketS2C<>(exampleScreen);
        honeyPacketServer.send(exampleScreenScreenPacketS2C);
        return false;
    }
}
