package io.github.thewebcode.honey.example;

import io.github.thewebcode.honey.netty.buffer.PacketBuffer;
import io.github.thewebcode.honey.netty.packet.HoneyPacket;

import java.nio.charset.StandardCharsets;

public class ExamplePacket extends HoneyPacket {
    private String aString;
    private int aNumber;

    //This Constructor is very important!!!
    public ExamplePacket() {
    }

    public ExamplePacket(String aString, int aNumber) {
        this.aString = aString;
        this.aNumber = aNumber;
    }

    public int getaNumber() {
        return aNumber;
    }

    public String getaString() {
        return aString;
    }

    @Override
    public void read(PacketBuffer buffer) {
        //Reading a String from the buffer
        int length = buffer.readInt();
        byte[] stringBytes = new byte[length];
        buffer.readBytes(stringBytes);
        String stringFromTheBuffer = new String(stringBytes, StandardCharsets.UTF_8);

        //Reading the number from the buffer
        int numberFromTheBuffer = buffer.readInt();

        //Assigning them to the attributes
        this.aString = stringFromTheBuffer;
        this.aNumber = numberFromTheBuffer;
    }

    @Override
    public void write(PacketBuffer buffer) {
        byte[] bytes = aString.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);

        buffer.writeInt(aNumber);
    }
}
