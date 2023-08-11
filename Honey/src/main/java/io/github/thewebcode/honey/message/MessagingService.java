package io.github.thewebcode.honey.message;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagingService {
    private final HashMap<Message, MessageReceiver> messages;
    private final HashMap<Player, ArrayList<Message>> playerMessages;

    public MessagingService() {
        this.messages = new HashMap<>();
        this.playerMessages = new HashMap<>();
    }

    public void addMessageToQueue(Message message, MessageReceiver receiver) {
        messages.put(message, receiver);
    }

    //Gets called every Message Tick (= 3 Sec)
    public void tick() {
        for (Message message : messages.keySet()) {
            MessageReceiver receiver = messages.get(message);

            switch (receiver) {
                case CONSOLE:
                    Bukkit.getConsoleSender().sendMessage(message.getContent());
                    messages.remove(message);
                    break;
                case ALL_SERVER_PLAYERS:
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        sendToPlayer(message, player);
                    });
                    messages.remove(message);
                    break;
                case PLAYER:
                    Player player = receiver.getReceiverAsPlayer();
                    ArrayList<Message> newPlayerMessages = new ArrayList<>();

                    if (playerMessages.containsKey(player)) {
                        ArrayList<Message> alreadyInMessages = playerMessages.get(player);
                        newPlayerMessages.addAll(alreadyInMessages);
                    }

                    messages.remove(message);
                    playerMessages.put(player, newPlayerMessages);
                    playerTick(player);
                    break;
            }
        }
    }

    private void playerTick(Player player) {
        ArrayList<Message> messagesForPlayer = playerMessages.get(player);
        Message highesPriorityMessage = null;

        for (Message message : messagesForPlayer) {
            if (highesPriorityMessage == null) {
                highesPriorityMessage = message;
                continue;
            }

            if (highesPriorityMessage.getPriority().getPriorityAsInt() < message.getPriority().getPriorityAsInt()) {
                highesPriorityMessage = message;
            }
        }

        messagesForPlayer.remove(highesPriorityMessage);

        if (messagesForPlayer.isEmpty()) {
            playerMessages.remove(player);
        } else {
            playerMessages.put(player, messagesForPlayer);
        }

        if (highesPriorityMessage == null) {
            playerMessages.remove(player);
            return;
        }

        sendToPlayer(highesPriorityMessage, player);
    }

    private void sendToPlayer(Message message, Player player) {
        String content = message.getContent();
        TextComponent text = Component.text(content);

        if (message instanceof ActionBarMessage) {
            player.sendActionBar(text);
            return;
        }

        if (message instanceof ChatMessage) {
            player.sendMessage(text);
        }
    }

}
