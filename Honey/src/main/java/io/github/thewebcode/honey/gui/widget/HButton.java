package io.github.thewebcode.honey.gui.widget;

public class HButton implements HWidget {
    private final String text;
    private final String buttonID;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public HButton(String text, String buttonID, int x, int y, int width, int height) {
        this.text = text;
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public HButton(String text, String buttonID, int x, int y) {
        this(text, buttonID, x, y, 1, 1);
    }

    public HButton(String text, String buttonID) {
        this(text, buttonID, 1, 1);
    }

    public String getText() {
        return text;
    }

    public String getButtonID() {
        return buttonID;
    }

    @Override
    public int posX() {
        return x;
    }

    @Override
    public int posY() {
        return y;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int heigth() {
        return height;
    }

    @Override
    public Type widgetType() {
        return Type.BUTTON;
    }
}
