package io.github.thewebcode.networking.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;

import java.util.ArrayList;

public class ServerGuiDescription extends LightweightGuiDescription {
    public ServerGuiDescription(WGridPanel rootPanel, ArrayList<ClientHWidget> widgets) {
        this(rootPanel, widgets, true);
    }

    public ServerGuiDescription(WGridPanel rootPanel, ArrayList<ClientHWidget> widgets, boolean validate) {
        setRootPanel(rootPanel);

        widgets.forEach(hWidget -> {
            int x = hWidget.getX();
            int y = hWidget.getY();
            int width = hWidget.getWidth();
            int height = hWidget.getHeight();

            WWidget widget = hWidget.getWidget();
            rootPanel.add(widget, x, y, width, height);
        });

        if (validate) rootPanel.validate(this);
    }
}
