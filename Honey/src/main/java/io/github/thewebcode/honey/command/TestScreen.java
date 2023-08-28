package io.github.thewebcode.honey.command;

import io.github.thewebcode.honey.gui.widget.*;

import java.util.Arrays;
import java.util.List;

public class TestScreen implements HScreen {
    @Override
    public List<HWidget> getWidgets() {
        return Arrays.asList(
                new HLabel("Test Screen", 4, 0, 4, 1),
                new HToggleButton("This Mod is nice", "button_mod_nice", false, 1, 2, 4, 1),
                new HButton("Close", "gui_button_close", 1, 4, 8, 2)
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
