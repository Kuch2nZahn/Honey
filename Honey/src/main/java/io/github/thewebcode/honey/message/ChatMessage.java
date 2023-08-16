package io.github.thewebcode.honey.message;

public class ChatMessage implements Message {
    private final MessageReceiver receiver;
    private final String content;
    private final Priority priority;

    public ChatMessage(String content, Priority priority) {
        this(content, priority, MessageReceiver.CONSOLE);
    }

    public ChatMessage(String content, Priority priority, MessageReceiver receiver) {
        this.receiver = receiver;
        this.content = content;
        this.priority = priority;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public MessageReceiver getReceiver() {
        return receiver;
    }
}
