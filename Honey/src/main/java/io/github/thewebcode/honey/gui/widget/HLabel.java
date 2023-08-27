package io.github.thewebcode.honey.gui.widget;

public class HLabel implements HWidget {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String text;
    private final Type widgetClassName;

    public HLabel(String text, int x, int y, int width, int height) {
        this.widgetClassName = Type.LABEL;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public HLabel(String text, int x, int y) {
        this(text, x, y, 1, 1);
    }

    public HLabel(String text) {
        this(text, 0, 0, 1, 1);
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
    public HWidget.Type widgetType() {
        return widgetClassName;
    }

    public String getText() {
        return text;
    }
}
