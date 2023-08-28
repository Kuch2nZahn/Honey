package io.github.thewebcode.networking.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class ServerGuiScreen extends CottonClientScreen {
    private final boolean closeableWithEsc;

    public ServerGuiScreen(GuiDescription description, boolean closeableWithEsc) {
        super(description);
        this.closeableWithEsc = closeableWithEsc;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return closeableWithEsc;
    }
}
