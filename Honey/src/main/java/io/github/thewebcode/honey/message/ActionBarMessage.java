package io.github.thewebcode.honey.message;

public class ActionBarMessage implements Message {
    private final MessageReceiver receiver;
    private final String content;
    private final Priority priority;

    public ActionBarMessage(String content, Priority messagePriority) {
        this(content, messagePriority, MessageReceiver.CONSOLE);
    }

    public ActionBarMessage(String content, Priority messagePriority, MessageReceiver receiver) {
        this.receiver = receiver;
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

    @Override
    public MessageReceiver getReceiver() {
        return receiver;
    }
}
