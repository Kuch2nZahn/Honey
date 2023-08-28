package io.github.thewebcode.honey.netty.packet.impl;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.nio.charset.StandardCharsets;

public class HoneyUpdateLanguageSettingC2SPacket extends HoneyPacket {
    private Language language;

    public HoneyUpdateLanguageSettingC2SPacket() {
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public void read(PacketBuffer buffer) {
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        String value = new String(bytes, StandardCharsets.UTF_8);
        this.language = Language.getByValue(value);
    }

    @Override
    public void write(PacketBuffer buffer) {
        String value = language.getValue();
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public enum Language {
        DE("de", "Deutsch"),
        EN("en", "english");

        private final String value;
        private final String title;

        Language(String value, String title) {
            this.value = value;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }

        public static Language getByValue(String language) {
            for (Language lang : Language.values()) {
                if (lang.getValue().equalsIgnoreCase(language)) return lang;
            }

            return null;
        }
    }
}
