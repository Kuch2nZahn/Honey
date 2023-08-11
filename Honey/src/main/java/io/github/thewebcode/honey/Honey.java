package io.github.thewebcode.honey;

import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.utils.MessageBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public final class Honey extends JavaPlugin {
    private static Honey instance;
    private ConfigManager configManager;
    private MessageBuilder messageBuilder;
    private final boolean devMode = true;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();
        this.messageBuilder = new MessageBuilder();

        System.out.println(messageBuilder.getWithKey("plugin_started"));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public static Honey getInstance() {
        return instance;
    }
}
