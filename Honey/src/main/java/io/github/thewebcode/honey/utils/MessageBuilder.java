package io.github.thewebcode.honey.utils;

import io.github.thewebcode.honey.Honey;
import io.github.thewebcode.honey.config.ConfigManager;
import io.github.thewebcode.honey.message.*;
import io.github.thewebcode.honey.netty.packet.impl.HoneyUpdateLanguageSettingC2SPacket;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MessageBuilder {
    private final String prefix;
    private final HashMap<Player, HoneyUpdateLanguageSettingC2SPacket.Language> playerLanguageMap;

    public MessageBuilder() {
        ConfigManager configManager = Honey.getInstance().getConfigManager();
        YamlConfiguration pluginConfig = configManager.getPluginConfig();

        if (!pluginConfig.contains("prefix")) {
            pluginConfig.set("prefix", "Honey");
            configManager.save();
        }

        this.prefix = pluginConfig.getString("prefix");
        this.playerLanguageMap = new HashMap<>();
    }

    public void putPlayerLanguage(Player player, HoneyUpdateLanguageSettingC2SPacket.Language language) {
        this.playerLanguageMap.put(player, language);
    }

    public HoneyUpdateLanguageSettingC2SPacket.Language getPlayerLanguage(Player player) {
        return playerLanguageMap.getOrDefault(player, HoneyUpdateLanguageSettingC2SPacket.Language.EN);
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

    public static String getTranslatableMessage(Player player, String key) {
        MessageBuilder messageBuilder = Honey.getInstance().getMessageBuilder();
        HoneyUpdateLanguageSettingC2SPacket.Language playerLanguage = messageBuilder.getPlayerLanguage(player);
        String value = playerLanguage.getValue();

        String messageKey = String.format("message.%s.%s", value, key);

        ConfigManager configManager = Honey.getInstance().getConfigManager();
        YamlConfiguration pluginConfig = configManager.getPluginConfig();

        return pluginConfig.isSet(messageKey) ? pluginConfig.getString(messageKey) : getMessage(key);
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
