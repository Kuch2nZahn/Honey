package io.github.thewebcode.honey.utils;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.message.*;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageBuilder {
    private final String prefix;

    public MessageBuilder() {
        ConfigManager configManager = Honey.getInstance().getConfigManager();
        YamlConfiguration pluginConfig = configManager.getPluginConfig();

        if (!pluginConfig.contains("prefix")) {
            pluginConfig.set("prefix", "Honey");
            configManager.save();
        }

        this.prefix = pluginConfig.getString("prefix");
    }

    public String addPrefix(String msg) {
        return String.format("%s %s", prefix, msg);
    }

    public String translateColorCodes(String msg) {
        return msg.replaceAll("&", "§");
    }

    public String buildMessage(String msg) {
        String withPrefix = addPrefix(msg);
        String translatedColorCodes = translateColorCodes(withPrefix);

        return translatedColorCodes;
    }

    public String getWithKey(String key) {
        ConfigManager configManager = Honey.getInstance().getConfigManager();
        YamlConfiguration pluginConfig = configManager.getPluginConfig();

        String language = pluginConfig.getString("language");
        if (language.equalsIgnoreCase("client")) language = "en";
        String fullkey = String.format("message.%s.%s", language, key);

        return pluginConfig.getString(fullkey);
    }

    public static String getMessage(String key) {
        MessageBuilder messageBuilder = Honey.getInstance().getMessageBuilder();
        return messageBuilder.getWithKey(key);
    }

    public static void buildChatMessageAndAddToQueue(String message, MessageReceiver receiver, Message.Priority priority) {
        Honey instance = Honey.getInstance();
        MessagingService messagingService = instance.getMessagingService();
        MessageBuilder messageBuilder = instance.getMessageBuilder();

        ChatMessage chatMessage = new ChatMessage(messageBuilder.buildMessage(message), priority, receiver);
        messagingService.addMessageToQueue(chatMessage);
    }

    public static void buildChatMessageAndAddToQueue(String message, MessageReceiver receiver) {
        buildChatMessageAndAddToQueue(message, receiver, Message.Priority.DEFAULT);
    }

    public static void buildActionBarMessageAndAddToQueue(String message, MessageReceiver receiver, Message.Priority priority) {
        Honey instance = Honey.getInstance();
        MessagingService messagingService = instance.getMessagingService();
        MessageBuilder messageBuilder = instance.getMessageBuilder();

        ActionBarMessage actionBarMessage = new ActionBarMessage(messageBuilder.buildMessage(message), priority, receiver);
        messagingService.addMessageToQueue(actionBarMessage);
    }

    public static void buildActionBarMessageAndAddToQueue(String message, MessageReceiver receiver) {
        buildActionBarMessageAndAddToQueue(message, receiver, Message.Priority.DEFAULT);
    }

    public static String build(String msg) {
        MessageBuilder messageBuilder = Honey.getInstance().getMessageBuilder();
        return messageBuilder.buildMessage(msg);
    }
}
