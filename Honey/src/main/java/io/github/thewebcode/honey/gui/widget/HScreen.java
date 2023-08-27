package io.github.thewebcode.honey.gui.widget;

import java.util.List;

public interface HScreen {
    List<HWidget> getWidgets();

    int sizeX();

    int sizeY();

    default boolean validate() {
        return true;
    }
}
