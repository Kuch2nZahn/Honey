package io.github.thewebcode.honey.event.impl;

import io.github.thewebcode.honey.event.Event;
import io.github.thewebcode.honey.netty.packet.impl.HoneyUpdateLanguageSettingC2SPacket;
import org.bukkit.entity.Player;

public class PlayerUpdateLanguageEvent extends Event {
    private final Player player;
    private final HoneyUpdateLanguageSettingC2SPacket.Language language;

    public PlayerUpdateLanguageEvent(Player player, HoneyUpdateLanguageSettingC2SPacket.Language language) {
        this.player = player;
        this.language = language;
    }

    public HoneyUpdateLanguageSettingC2SPacket.Language getLanguage() {
        return language;
    }

    public Player getPlayer() {
        return player;
    }
}
