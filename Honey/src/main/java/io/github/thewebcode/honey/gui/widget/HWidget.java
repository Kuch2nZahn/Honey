package io.github.thewebcode.honey.gui.widget;

public interface HWidget {
    int posX();

    int posY();

    int width();

    int heigth();

    Type widgetType();

    enum Type {
        LABEL,
        TOGGLE_BUTTON,
        UNKNOWN
    }
}
