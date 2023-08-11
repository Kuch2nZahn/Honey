package io.github.thewebcode.honey;

import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.message.MessageReceiver;
import io.github.thewebcode.honey.message.MessagingService;
import io.github.thewebcode.honey.utils.MessageBuilder;
import io.github.thewebcode.honey.utils.TimingService;
import org.bukkit.plugin.java.JavaPlugin;

public final class Honey extends JavaPlugin {
    private static Honey instance;
    private ConfigManager configManager;
    private MessageBuilder messageBuilder;
    private MessagingService messagingService;
    private TimingService timingService;
    private final boolean devMode = true;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();
        this.messageBuilder = new MessageBuilder();
        this.messagingService = new MessagingService();
        this.timingService = new TimingService();

        String message = messageBuilder.getWithKey("plugin_started");
        MessageBuilder.buildChatMessageAndAddToQueue(message, MessageReceiver.CONSOLE);
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

    public boolean isDevMode() {
        return devMode;
    }

    public static Honey getInstance() {
        return instance;
    }
}
