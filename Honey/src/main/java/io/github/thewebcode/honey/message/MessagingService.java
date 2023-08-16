package io.github.thewebcode.honey.message;


import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class MessagingService {
    private final Queue<Message> defaultQueue;
    private final Queue<Message> priorityQueue;

    public MessagingService() {
        this.defaultQueue = new LinkedList<>();
        this.priorityQueue = new LinkedList<>();
    }

    public void addMessageToQueue(Message message) {
        Message.Priority priority = message.getPriority();
        switch (priority) {
            case LOW, DEFAULT -> defaultQueue.add(message);
            case MEDIUM, HIGH -> priorityQueue.add(message);
            case HIGHEST -> sendMessage(message);
        }
    }

    public void tick() {
        if (!priorityQueue.isEmpty()) {
            List<Message> highMessages = priorityQueue.stream().filter(message -> message.getPriority().equals(Message.Priority.HIGH)).collect(Collectors.toList());

            if (!highMessages.isEmpty()) {
                highMessages.forEach(message -> {
                    sendMessage(message);
                    priorityQueue.remove(message);
                });
                return;
            }

            sendMessage(priorityQueue.remove());
            return;
        }

        if (defaultQueue.isEmpty()) return;

        List<Message> defaultMessages = defaultQueue.stream().filter(message -> message.getPriority().equals(Message.Priority.DEFAULT)).collect(Collectors.toList());

        if (!defaultMessages.isEmpty()) {
            Message message = defaultMessages.get(0);
            defaultQueue.remove(message);
            sendMessage(message);
            return;
        }

        sendMessage(defaultQueue.remove());
    }

    private void sendMessage(Message message) {
        MessageReceiver receiver = message.getReceiver();

        switch (receiver) {
            case CONSOLE:
                Bukkit.getConsoleSender().sendMessage(message.getContent());
                break;
            case ALL_SERVER_PLAYERS:
                Bukkit.getOnlinePlayers().forEach(p -> sendToPlayer(message, p));
                break;
            case PLAYER:
                Optional<Player> optionalPlayer = message.getReceiver().getReceiverAsPlayer();
                Player player = optionalPlayer.orElseThrow();
                sendToPlayer(message, player);
                break;
        }
    }

    private void sendToPlayer(Message message, Player player) {
        if (message instanceof ChatMessage) {
            player.sendMessage(message.getContent());
            return;
        }

        if (message instanceof ActionBarMessage) {
            player.sendActionBar(Component.text(message.getContent()));
        }
    }
}
