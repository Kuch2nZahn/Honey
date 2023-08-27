package io.github.thewebcode.gui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class ServerHoneyClientStartupLoadingScreen extends CottonClientScreen {
    public ServerHoneyClientStartupLoadingScreen(String loadingText) {
        super(new ServerHoneyClientStartupLoadingScreenDescription(loadingText));
    }

    @Override
    public void close() {
    }
}
