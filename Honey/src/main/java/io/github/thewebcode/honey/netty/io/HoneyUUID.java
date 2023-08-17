package io.github.thewebcode.honey.netty.io;

public enum HoneyUUID {

    SERVER("0000"),
    ALL_PLAYERS("0000");

    private final String value;

    HoneyUUID(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
