package io.github.thewebcode.honey.example;

import io.github.thewebcode.honey.gui.widget.HButton;
import io.github.thewebcode.honey.gui.widget.HLabel;
import io.github.thewebcode.honey.gui.widget.HScreen;
import io.github.thewebcode.honey.gui.widget.HWidget;
import io.github.thewebcode.honey.netty.HoneyPacketServer;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyScreenS2CPacket;

import java.util.Arrays;
import java.util.List;

public class ExampleScreen implements HScreen {
    @Override
    public List<HWidget> getWidgets() {
        return Arrays.asList(
                new HLabel("Example Screen", 4, 0, 4, 1),
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

    public void openGui() {
        HoneyScreenS2CPacket packet = new HoneyScreenS2CPacket();
        packet.setScreen(new ExampleScreen());
        packet.setSenderUUID(HoneyUUID.SERVER);
        packet.setReceiverUUID(HoneyUUID.ALL_PLAYERS);

        HoneyPacketServer.sendPacket(packet);
    }
}
