package io.github.thewebcode.honey.event;

public class EventCancelable extends Event {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setcancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
