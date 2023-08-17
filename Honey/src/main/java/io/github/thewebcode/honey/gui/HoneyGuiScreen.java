package io.github.thewebcode.honey.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;

import java.io.Serializable;

public abstract class HoneyGuiScreen implements Serializable {
    private final GuiDescription description;

    public HoneyGuiScreen(GuiDescription description) {
        this.description = description;
    }

    public GuiDescription getDescription() {
        return description;
    }
}
