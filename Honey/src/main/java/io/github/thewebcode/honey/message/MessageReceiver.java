package io.github.thewebcode.honey.message;

import org.bukkit.entity.Player;

public enum MessageReceiver {
    CONSOLE,
    ALL_SERVER_PLAYERS,
    PLAYER;

    private Player receiver;

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public Player getReceiverAsPlayer() {
        return receiver;
    }
}
