package io.github.thewebcode.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;

public class ServerHoneyClientStartupLoadingScreenDescription extends LightweightGuiDescription {
    public ServerHoneyClientStartupLoadingScreenDescription(String loadingText) {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(240, 90);
        root.setInsets(Insets.ROOT_PANEL);
        WLabel label = new WLabel(Text.literal("Honey by TheWebcode"), 0xFFA500);
        label.setVerticalAlignment(VerticalAlignment.CENTER);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);

        WLabel currentTask = new WLabel(Text.literal(loadingText));
        currentTask.setHorizontalAlignment(HorizontalAlignment.CENTER);
        currentTask.setVerticalAlignment(VerticalAlignment.CENTER);

        root.add(label, 4, 0, 4, 1);
        root.add(currentTask, 4, 2, 4, 1);
    }
}
