package io.github.thewebcode.honey.netty.packet.impl.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thewebcode.honey.gui.widget.HScreen;
import io.github.thewebcode.honey.gui.widget.HWidget;
import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;
import io.github.thewebcode.honey.utils.InterfaceAdapter;
import io.github.thewebcode.honey.utils.ObjectHelper;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class HoneyScreenS2CPacket extends HoneyPacket {
    private final Gson gson;
    private Collection<String> serializedWidgets;
    private boolean validate;
    private boolean canCloseWithEsc;
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
            serializedWidgets.add(json);
        });

        this.validate = screen.validate();
        this.sizeX = screen.sizeX();
        this.sizeY = screen.sizeY();
        this.canCloseWithEsc = screen.canCloseWithEsc();
    }

    public boolean shouldValidate() {
        return this.validate;
    }

    public boolean canCloseWithEsc() {
        return canCloseWithEsc;
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
        buffer.resetReaderIndex();
        int length = buffer.readInt();
        byte[] jsonStringBytes = new byte[length];
        ByteBuf byteBuf = buffer.readBytes(length);
        byteBuf.readBytes(jsonStringBytes);
        String jsonString = new String(jsonStringBytes, StandardCharsets.UTF_8);
        Collection<String> collection = gson.fromJson(jsonString, Collection.class);

        this.serializedWidgets = collection;
        this.validate = buffer.readBoolean();
        this.canCloseWithEsc = buffer.readBoolean();
        this.sizeX = buffer.readInt();
        this.sizeY = buffer.readInt();

        buffer.clear();
        buffer.resetReaderIndex();
        buffer.resetWriterIndex();
    }

    @Override
    public void write(PacketBuffer buffer) {
        //buffer.writeStringCollection(serializedWidgets);

        String jsonString = ObjectHelper.gson.toJson(serializedWidgets);

        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);

        buffer.writeBoolean(validate);
        buffer.writeBoolean(canCloseWithEsc);
        buffer.writeInt(sizeX);
        buffer.writeInt(sizeY);
    }
}
