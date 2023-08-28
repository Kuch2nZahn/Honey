package io.github.thewebcode.honey;

import io.github.thewebcode.honey.command.TestCommand;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.event.EventListener;
import io.github.thewebcode.honey.event.EventManager;
import io.github.thewebcode.honey.event.PacketEventListener;
import io.github.thewebcode.honey.message.Message;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.message.MessagingService;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.utils.MessageBuilder;
import io.github.thewebcode.honey.utils.TimingService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
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
    private EventListener eventListener;
    private final boolean devMode = true;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();
        this.messageBuilder = new MessageBuilder();
        this.messagingService = new MessagingService();
        this.timingService = new TimingService();
        this.packetRegistry = new HoneyPacketRegistry();
        this.packetEventRegistry = new PacketEventRegistry(HoneyUUID.SERVER.getValue());
        this.honeyPacketServer = new HoneyPacketServer(this.packetRegistry, (future) -> {
        }, packetEventRegistry);
        packetEventRegistry.registerEvents(new PacketEventListener());

        this.eventListener = new EventListener();

        String message = messageBuilder.getWithKey("plugin_started");
        MessageBuilder.buildChatMessageAndAddToQueue(message, MessageReceiver.CONSOLE, Message.Priority.HIGH);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Honey.getInstance().shutdown();
        }));

        registerCommands();
        registerEvents();
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

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(eventListener, this);

        EventManager.register(eventListener);
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

    public EventListener getEventListener() {
        return eventListener;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public static Honey getInstance() {
        return instance;
    }
}
