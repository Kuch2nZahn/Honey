package io.github.thewebcode.honey.netty.packet.impl.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thewebcode.honey.gui.widget.HScreen;
import io.github.thewebcode.honey.gui.widget.HWidget;
import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.utils.InterfaceAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class HoneyScreenS2CPacket extends HoneyPacket {
    private final Gson gson;
    private Collection<String> serializedWidgets;
    private boolean validate;
    private int sizeX;
    private int sizeY;

    public HoneyScreenS2CPacket() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HWidget.class, new InterfaceAdapter<HWidget>());

        this.gson = gsonBuilder.create();
        this.serializedWidgets = new ArrayList<>();
    }

    public void setScreen(HScreen screen) {
        screen.getWidgets().forEach(hWidget -> {
            String json = gson.toJson(hWidget, HWidget.class);
            System.out.println("json = " + json);
            serializedWidgets.add(json);
        });

        this.validate = screen.validate();
        this.sizeX = screen.sizeX();
        this.sizeY = screen.sizeY();
    }

    public boolean shouldValidate() {
        return this.validate;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public ArrayList<HWidget> getScreenWidgets() {
        ArrayList<HWidget> widgets = new ArrayList<>();

        serializedWidgets.forEach(json -> {
            HWidget hWidget = gson.fromJson(json, HWidget.class);
            widgets.add(hWidget);
        });

        return widgets;
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.serializedWidgets = buffer.readStringCollection();
        this.validate = buffer.readBoolean();
        this.sizeX = buffer.readInt();
        this.sizeY = buffer.readInt();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeStringCollection(serializedWidgets);
        buffer.writeBoolean(validate);
        buffer.writeInt(sizeX);
        buffer.writeInt(sizeY);
    }
}
