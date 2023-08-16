package io.github.thewebcode.honey.exception;

public class PacketRegistrationException extends Exception {
    public PacketRegistrationException(String message) {
        super(message);
    }

    public PacketRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
