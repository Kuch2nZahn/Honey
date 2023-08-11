package io.github.thewebcode.honey.message;

public class ActionBarMessage implements Message {
    private final String content;
    private final Priority priority;

    public ActionBarMessage(String content, Priority messagePriority) {
        this.content = content;
        this.priority = messagePriority;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Message.Priority getPriority() {
        return priority;
    }
}
