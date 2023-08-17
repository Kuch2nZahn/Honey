package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.gui.ExampleScreen;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.packet.impl.gui.ScreenPacketS2C;
import io.github.thewebcode.honey.utils.ObjectHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        HoneyPacketServer honeyPacketServer = Honey.getInstance().getHoneyPacketServer();
        ScreenPacketS2C screenPacketS2C = new ScreenPacketS2C();
        String json = ObjectHelper.gson.toJson(new ExampleScreen(null));
        System.out.println("json = " + json);
        screenPacketS2C.setScreen(json);
        honeyPacketServer.send(screenPacketS2C);
        return false;
    }
}
