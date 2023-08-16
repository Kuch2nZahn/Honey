package io.github.thewebcode.honey.message;

public interface Message {
    MessageReceiver getReceiver();
    String getContent();

    Priority getPriority();

    enum Priority {
        HIGHEST(3),
        HIGH(2),
        MEDIUM(1),
        DEFAULT(0),
        LOW(-1);

        int priority;

        Priority(int priority) {
            this.priority = priority;
        }

        public int getPriorityAsInt() {
            return priority;
        }
    }
}
