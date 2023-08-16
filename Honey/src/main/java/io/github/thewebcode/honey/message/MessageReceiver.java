package io.github.thewebcode.honey.message;

import org.bukkit.entity.Player;

import java.util.Optional;

public enum MessageReceiver {
    CONSOLE,
    ALL_SERVER_PLAYERS,
    PLAYER;

    private Optional<Player> receiver;

    public void setReceiver(Player receiver) {
        this.receiver = Optional.of(receiver);
    }

    public Optional<Player> getReceiverAsPlayer() {
        return receiver;
    }
}
