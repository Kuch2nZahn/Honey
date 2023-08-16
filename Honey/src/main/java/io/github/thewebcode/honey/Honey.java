package io.github.thewebcode.honey;

import io.github.thewebcode.honey.command.TestCommand;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.message.Message;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.message.MessagingService;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.utils.MessageBuilder;
import io.github.thewebcode.honey.utils.TimingService;
import org.bukkit.plugin.java.JavaPlugin;

public final class Honey extends JavaPlugin {
    private static Honey instance;
    private ConfigManager configManager;
    private MessageBuilder messageBuilder;
    private MessagingService messagingService;
    private TimingService timingService;
    private HoneyPacketRegistry packetRegistry;
    private PacketEventRegistry packetEventRegistry;
    private HoneyPacketServer honeyPacketServer;
    private final boolean devMode = true;
    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();
        this.messageBuilder = new MessageBuilder();
        this.messagingService = new MessagingService();
        this.timingService = new TimingService();
        this.packetRegistry = new HoneyPacketRegistry();
        this.packetEventRegistry = new PacketEventRegistry();
        this.honeyPacketServer = new HoneyPacketServer(this.packetRegistry, (future) -> {
        }, packetEventRegistry);

        String message = messageBuilder.getWithKey("plugin_started");
        MessageBuilder.buildChatMessageAndAddToQueue(message, MessageReceiver.CONSOLE, Message.Priority.HIGH);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Honey.getInstance().shutdown();
        }));

        registerCommands();
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    private void shutdown() {
        honeyPacketServer.shutdown();
    }

    private void registerCommands() {
        getCommand("test").setExecutor(new TestCommand());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public MessagingService getMessagingService() {
        return messagingService;
    }

    public TimingService getTimingService() {
        return timingService;
    }

    public HoneyPacketServer getHoneyPacketServer() {
        return honeyPacketServer;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public static Honey getInstance() {
        return instance;
    }
}
