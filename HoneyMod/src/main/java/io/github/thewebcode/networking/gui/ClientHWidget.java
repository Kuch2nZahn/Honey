package io.github.thewebcode.networking.gui;

import io.github.cottonmc.cotton.gui.widget.WWidget;

public class ClientHWidget<T extends WWidget> {
    private final T widget;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public ClientHWidget(T widget, int x, int y, int width, int height) {
        this.widget = widget;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public T getWidget() {
        return widget;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
