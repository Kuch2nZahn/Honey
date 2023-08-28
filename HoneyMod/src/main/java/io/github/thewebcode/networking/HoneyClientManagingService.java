package io.github.thewebcode.networking;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.thewebcode.HoneyMod;
import io.github.thewebcode.honey.gui.widget.HButton;
import io.github.thewebcode.honey.gui.widget.HLabel;
import io.github.thewebcode.honey.gui.widget.HToggleButton;
import io.github.thewebcode.honey.gui.widget.HWidget;
import io.github.thewebcode.honey.netty.HoneyClient;
import io.github.thewebcode.honey.netty.event.PacketEventRegistry;
import io.github.thewebcode.honey.netty.event.PacketSubscriber;
import io.github.thewebcode.honey.netty.io.HoneyUUID;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.netty.packet.impl.HoneyCloseScreenS2CPacket;
import io.github.thewebcode.honey.netty.packet.impl.HoneyToastS2CPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyGuiButtonPressedC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyGuiButtonToggledC2SPacket;
import io.github.thewebcode.honey.netty.packet.impl.gui.HoneyScreenS2CPacket;
import io.github.thewebcode.honey.netty.registry.HoneyPacketRegistry;
import io.github.thewebcode.honey.netty.registry.IPacketRegistry;
import io.github.thewebcode.networking.gui.ClientHWidget;
import io.github.thewebcode.networking.gui.ServerGuiDescription;
import io.github.thewebcode.networking.gui.ServerGuiScreen;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class HoneyClientManagingService {
    private HoneyClient honeyClient;
    private final PacketEventRegistry packetEventRegistry;
    private final IPacketRegistry packetRegistry;

    public HoneyClientManagingService() {
        this.packetRegistry = new HoneyPacketRegistry();
        this.packetEventRegistry = new PacketEventRegistry(MinecraftClient.getInstance().getSession().getProfile().getId().toString());

        this.packetEventRegistry.registerEvents(new Object() {
            @PacketSubscriber
            public void onToastPacketReceive(HoneyToastS2CPacket packet, ChannelHandlerContext context) {
                HoneyToastS2CPacket.Type rawType = packet.getType();
                SystemToast.Type type = SystemToast.Type.valueOf(rawType.toString());
                Text title = Text.literal(packet.getTitle());
                Text description = Text.literal(packet.getDescription());
                Toast toast = new SystemToast(type, title, description);
                MinecraftClient.getInstance().getToastManager().add(toast);
            }

            @PacketSubscriber
            public void onCloseScreenPacket(HoneyCloseScreenS2CPacket packet) {
                MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().currentScreen.close());
            }

            @PacketSubscriber
            public void onScreenPacketReceive(HoneyScreenS2CPacket packet, ChannelHandlerContext context) {
                context.flush();
                WGridPanel root = new WGridPanel();

                root.setInsets(Insets.ROOT_PANEL);
                int rootSizeX = packet.getSizeX();
                int rootSizeY = packet.getSizeY();
                root.setSize(rootSizeX, rootSizeY);

                ArrayList<ClientHWidget> widgets = new ArrayList<>();

                packet.getScreenWidgets().forEach(hWidget -> {
                    try {
                        HWidget.Type type = hWidget.widgetType();
                        int x = hWidget.posX();
                        int y = hWidget.posY();
                        int width = hWidget.width();
                        int height = hWidget.heigth();

                        switch (type) {
                            case LABEL:
                                HLabel hLabel = (HLabel) hWidget;
                                MutableText labelText = Text.literal(hLabel.getText());
                                WLabel wLabel = new WLabel(labelText);
                                root.add(wLabel, x, y, width, height);
                                break;
                            case TOGGLE_BUTTON:
                                HToggleButton hToggleButton = (HToggleButton) hWidget;
                                MutableText toggleButtonText = Text.literal(hToggleButton.getText());
                                boolean toggleButtonToggled = hToggleButton.isToggled();

                                WToggleButton wToggleButton = new WToggleButton(toggleButtonText);
                                wToggleButton.setToggle(toggleButtonToggled);
                                wToggleButton.setOnToggle((toggle) -> {
                                    HoneyGuiButtonToggledC2SPacket honeyGuiButtonToggledC2SPacket = new HoneyGuiButtonToggledC2SPacket();
                                    honeyGuiButtonToggledC2SPacket.setButtonID(hToggleButton.getButtonID());
                                    honeyGuiButtonToggledC2SPacket.setNewValue(toggle);
                                    honeyGuiButtonToggledC2SPacket.setReceiverUUID(HoneyUUID.SERVER);

                                    HoneyClientManagingService.sendPacket(honeyGuiButtonToggledC2SPacket);
                                });
                                root.add(wToggleButton, x, y, width, height);
                                break;
                            case BUTTON:
                                HButton hButton = (HButton) hWidget;
                                MutableText buttonText = Text.literal(hButton.getText());

                                WButton wButton = new WButton(buttonText);

                                wButton.setOnClick(() -> {
                                    HoneyGuiButtonPressedC2SPacket honeyGuiButtonPressedC2SPacket = new HoneyGuiButtonPressedC2SPacket();
                                    honeyGuiButtonPressedC2SPacket.setButtonID(hButton.getButtonID());
                                    honeyGuiButtonPressedC2SPacket.setReceiverUUID(HoneyUUID.SERVER);
                                    HoneyClientManagingService.sendPacket(honeyGuiButtonPressedC2SPacket);
                                });
                                root.add(wButton, x, y, width, height);
                                break;
                            case UNKNOWN:
                                throw new IllegalStateException("Screen Packet contains unknown widget!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


                ServerGuiDescription description = new ServerGuiDescription(root, widgets);
                if (packet.shouldValidate()) root.validate(description);

                ServerGuiScreen screen = new ServerGuiScreen(description, packet.canCloseWithEsc());

                MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreen(screen));
            }
        });
    }

    public void secureStart(InetSocketAddress toConnect) {
        if (this.honeyClient != null) shutdown();
        start(toConnect);
    }

    public void secureStartWithCallback(InetSocketAddress toConnect, Consumer<Future<? super Void>> callback) throws InterruptedException {
        if (this.honeyClient != null) shutdown();
        this.honeyClient = new HoneyClient(toConnect, this.packetRegistry, callback, packetEventRegistry);
    }

    public void start(InetSocketAddress toConnect) {
        try {
            this.honeyClient = new HoneyClient(toConnect, this.packetRegistry, (future) -> {
            }, packetEventRegistry);
        } catch (InterruptedException e) {
            System.out.println("Could not start local HoneyClient. Please Contact Developer with Error Code CEM1");
        }
    }

    public void shutdown() {
        if (this.honeyClient == null) return;

        honeyClient.shutdown();
        this.honeyClient = null;
    }

    public void send(HoneyPacket packet) {
        if (honeyClient == null) return;
        UUID id = MinecraftClient.getInstance().getSession().getProfile().getId();
        packet.setSenderUUID(id.toString());
        honeyClient.send(packet);
    }

    public static void sendPacket(HoneyPacket packet) {
        get().send(packet);
    }

    public HoneyClient getHoneyClient() {
        return honeyClient;
    }

    public static HoneyClientManagingService get() {
        return HoneyMod.getInstance().getHoneyClientManagingService();
    }
}
