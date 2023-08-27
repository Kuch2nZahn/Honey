package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.gui.widget.HLabel;
import io.github.thewebcode.honey.gui.widget.HScreen;
import io.github.thewebcode.honey.gui.widget.HToggleButton;
import io.github.thewebcode.honey.gui.widget.HWidget;

import java.util.Arrays;
import java.util.List;

public class TestScreen implements HScreen {
    @Override
    public List<HWidget> getWidgets() {
        return Arrays.asList(
                new HLabel("Test Screen", 4, 0, 4, 1),
                new HToggleButton("This Mod is nice", "button_mod_nice", false, 1, 2, 4, 1)
        );
    }

    @Override
    public int sizeX() {
        return 240;
    }

    @Override
    public int sizeY() {
        return 90;
    }
}
