package io.github.thewebcode.honey.netty.packet.impl;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.util.Arrays;
import java.util.List;

public class HoneyToastS2CPacket extends HoneyPacket {
    private Type type;
    private String title;
    private String description;

    public HoneyToastS2CPacket() {
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void read(PacketBuffer buffer) {
        List<String> strings = buffer.readStringCollection();
        this.type = HoneyToastS2CPacket.Type.valueOf(strings.get(0));
        this.title = strings.get(1);
        this.description = strings.get(2);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(Arrays.asList(type.toString(), title, description));
    }

    public enum Type {
        TUTORIAL_HINT,
        NARRATOR_TOGGLE,
        WORLD_BACKUP,
        PACK_LOAD_FAILURE,
        WORLD_ACCESS_FAILURE,
        PACK_COPY_FAILURE,
        PERIODIC_NOTIFICATION,
        UNSECURE_SERVER_WARNING
    }
}
