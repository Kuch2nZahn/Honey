package io.github.thewebcode.honey.netty.buffer;

import io.github.thewebcode.honey.netty.io.CallableDecoder;
import io.github.thewebcode.honey.netty.io.CallableEncoder;
import io.github.thewebcode.honey.netty.io.Decoder;
import io.github.thewebcode.honey.netty.io.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketBuffer extends ByteBuf {
    private final ByteBuf internalBuffer;

    public PacketBuffer(ByteBuf internalBuffer) {
        this.internalBuffer = internalBuffer;
    }

    public PacketBuffer() {
        this(Unpooled.buffer());
    }

    public void writeUUID(UUID value) {
        if (writableBytes() < 16) {
            ensureWritable(16);
        }
        writeLong(value.getMostSignificantBits());
        writeLong(value.getLeastSignificantBits());
    }

    public UUID readUUID() {
        if (readableBytes() < 16) {
            throw new IndexOutOfBoundsException("Not enough readableBytes to read UUID: " + readableBytes() + " / 16");
        }
        return new UUID(readLong(), readLong());
    }

    public void writeUTF8(String value) {
        if (value == null) {
            value = "";
        }
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeInt(bytes.length);
        writeBytes(bytes);
    }

    public String readUTF8() {
        int length = readInt();
        byte[] data = new byte[length];
        readBytes(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public <T extends Encoder> void writeCollection(Collection<T> collection) {
        writeCollection(collection, Encoder::write);
    }

    public <T extends Decoder> List<T> readCollection(Supplier<T> factory) {
        return readCollection(buffer -> {
            T instance = factory.get();
            instance.read(this);
            return instance;
        });
    }

    public <T> void writeCollection(Collection<T> collection, CallableEncoder<T> encoder) {
        writeInt(collection.size());
        for (T entry : collection) {
            encoder.write(entry, this);
        }
    }

    public <T> List<T> readCollection(CallableDecoder<T> decoder) {
        int size = readInt();
        List<T> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(decoder.read(this));
        }
        return data;
    }

    public void writeIntCollection(Collection<Integer> collection) {
        writeCollection(collection, (data, buffer) -> buffer.writeInt(data));
    }

    public List<Integer> readIntCollection() {
        return readCollection(PacketBuffer::readInt);
    }

    public void writeStringCollection(Collection<String> collection) {
        writeCollection(collection, (data, buffer) -> buffer.writeUTF8(data));
    }

    public List<String> readStringCollection() {
        return readCollection(PacketBuffer::readUTF8);
    }

    public void writeUuidCollection(Collection<UUID> collection) {
        writeCollection(collection, (data, buffer) -> buffer.writeUUID(data));
    }

    public List<UUID> readUuidCollection() {
        return readCollection(PacketBuffer::readUUID);
    }

    public int capacity() {
        return internalBuffer.capacity();
    }

    public ByteBuf capacity(int newCapacity) {
        return internalBuffer.capacity(newCapacity);
    }

    public int maxCapacity() {
        return internalBuffer.maxCapacity();
    }

    public ByteBufAllocator alloc() {
        return internalBuffer.alloc();
    }

    public ByteOrder order() {
        return internalBuffer.order();
    }

    public ByteBuf order(ByteOrder endianness) {
        return internalBuffer.order(endianness);
    }

    public ByteBuf unwrap() {
        return internalBuffer.unwrap();
    }

    public boolean isDirect() {
        return internalBuffer.isDirect();
    }

    public boolean isReadOnly() {
        return internalBuffer.isReadOnly();
    }

    public ByteBuf asReadOnly() {
        return internalBuffer.asReadOnly();
    }

    public int readerIndex() {
        return internalBuffer.readerIndex();
    }

    public ByteBuf readerIndex(int readerIndex) {
        return internalBuffer.readerIndex(readerIndex);
    }

    public int writerIndex() {
        return internalBuffer.writerIndex();
    }

    public ByteBuf writerIndex(int writerIndex) {
        return internalBuffer.writerIndex(writerIndex);
    }

    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return internalBuffer.setIndex(readerIndex, writerIndex);
    }

    public int readableBytes() {
        return internalBuffer.readableBytes();
    }

    public int writableBytes() {
        return internalBuffer.writableBytes();
    }

    public int maxWritableBytes() {
        return internalBuffer.maxWritableBytes();
    }

    public boolean isReadable() {
        return internalBuffer.isReadable();
    }

    public boolean isReadable(int size) {
        return internalBuffer.isReadable(size);
    }

    public boolean isWritable() {
        return internalBuffer.isWritable();
    }

    public boolean isWritable(int size) {
        return internalBuffer.isWritable(size);
    }

    public ByteBuf clear() {
        return internalBuffer.clear();
    }

    public ByteBuf markReaderIndex() {
        return internalBuffer.markReaderIndex();
    }

    public ByteBuf resetReaderIndex() {
        return internalBuffer.resetReaderIndex();
    }

    public ByteBuf markWriterIndex() {
        return internalBuffer.markWriterIndex();
    }

    public ByteBuf resetWriterIndex() {
        return internalBuffer.resetWriterIndex();
    }

    public ByteBuf discardReadBytes() {
        return internalBuffer.discardReadBytes();
    }

    public ByteBuf discardSomeReadBytes() {
        return internalBuffer.discardSomeReadBytes();
    }

    public ByteBuf ensureWritable(int minWritableBytes) {
        return internalBuffer.ensureWritable(minWritableBytes);
    }

    public int ensureWritable(int minWritableBytes, boolean force) {
        return internalBuffer.ensureWritable(minWritableBytes, force);
    }

    public boolean getBoolean(int index) {
        return internalBuffer.getBoolean(index);
    }

    public byte getByte(int index) {
        return internalBuffer.getByte(index);
    }

    public short getUnsignedByte(int index) {
        return internalBuffer.getUnsignedByte(index);
    }

    public short getShort(int index) {
        return internalBuffer.getShort(index);
    }

    public short getShortLE(int index) {
        return internalBuffer.getShortLE(index);
    }

    public int getUnsignedShort(int index) {
        return internalBuffer.getUnsignedShort(index);
    }

    public int getUnsignedShortLE(int index) {
        return internalBuffer.getUnsignedShortLE(index);
    }

    public int getMedium(int index) {
        return internalBuffer.getMedium(index);
    }

    public int getMediumLE(int index) {
        return internalBuffer.getMediumLE(index);
    }

    public int getUnsignedMedium(int index) {
        return internalBuffer.getUnsignedMedium(index);
    }

    public int getUnsignedMediumLE(int index) {
        return internalBuffer.getUnsignedMediumLE(index);
    }

    public int getInt(int index) {
        return internalBuffer.getInt(index);
    }

    public int getIntLE(int index) {
        return internalBuffer.getIntLE(index);
    }

    public long getUnsignedInt(int index) {
        return internalBuffer.getUnsignedInt(index);
    }

    public long getUnsignedIntLE(int index) {
        return internalBuffer.getUnsignedIntLE(index);
    }

    public long getLong(int index) {
        return internalBuffer.getLong(index);
    }

    public long getLongLE(int index) {
        return internalBuffer.getLongLE(index);
    }

    public char getChar(int index) {
        return internalBuffer.getChar(index);
    }

    public float getFloat(int index) {
        return internalBuffer.getFloat(index);
    }

    public double getDouble(int index) {
        return internalBuffer.getDouble(index);
    }

    public ByteBuf getBytes(int index, ByteBuf dst) {
        return internalBuffer.getBytes(index, dst);
    }

    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return internalBuffer.getBytes(index, dst, length);
    }

    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return internalBuffer.getBytes(index, dst, dstIndex, length);
    }

    public ByteBuf getBytes(int index, byte[] dst) {
        return internalBuffer.getBytes(index, dst);
    }

    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return internalBuffer.getBytes(index, dst, dstIndex, length);
    }

    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return internalBuffer.getBytes(index, dst);
    }

    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return internalBuffer.getBytes(index, out, length);
    }

    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return internalBuffer.getBytes(index, out, length);
    }

    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return internalBuffer.getBytes(index, out, position, length);
    }

    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return internalBuffer.getCharSequence(index, length, charset);
    }

    public ByteBuf setBoolean(int index, boolean value) {
        return internalBuffer.setBoolean(index, value);
    }

    public ByteBuf setByte(int index, int value) {
        return internalBuffer.setByte(index, value);
    }

    public ByteBuf setShort(int index, int value) {
        return internalBuffer.setShort(index, value);
    }

    public ByteBuf setShortLE(int index, int value) {
        return internalBuffer.setShortLE(index, value);
    }

    public ByteBuf setMedium(int index, int value) {
        return internalBuffer.setMedium(index, value);
    }

    public ByteBuf setMediumLE(int index, int value) {
        return internalBuffer.setMediumLE(index, value);
    }

    public ByteBuf setInt(int index, int value) {
        return internalBuffer.setInt(index, value);
    }

    public ByteBuf setIntLE(int index, int value) {
        return internalBuffer.setIntLE(index, value);
    }

    public ByteBuf setLong(int index, long value) {
        return internalBuffer.setLong(index, value);
    }

    public ByteBuf setLongLE(int index, long value) {
        return internalBuffer.setLongLE(index, value);
    }

    public ByteBuf setChar(int index, int value) {
        return internalBuffer.setChar(index, value);
    }

    public ByteBuf setFloat(int index, float value) {
        return internalBuffer.setFloat(index, value);
    }

    public ByteBuf setDouble(int index, double value) {
        return internalBuffer.setDouble(index, value);
    }

    public ByteBuf setBytes(int index, ByteBuf src) {
        return internalBuffer.setBytes(index, src);
    }

    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return internalBuffer.setBytes(index, src, length);
    }

    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return internalBuffer.setBytes(index, src, srcIndex, length);
    }

    public ByteBuf setBytes(int index, byte[] src) {
        return internalBuffer.setBytes(index, src);
    }

    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return internalBuffer.setBytes(index, src, srcIndex, length);
    }

    public ByteBuf setBytes(int index, ByteBuffer src) {
        return internalBuffer.setBytes(index, src);
    }

    public int setBytes(int index, InputStream in, int length) throws IOException {
        return internalBuffer.setBytes(index, in, length);
    }

    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return internalBuffer.setBytes(index, in, length);
    }

    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return internalBuffer.setBytes(index, in, position, length);
    }

    public ByteBuf setZero(int index, int length) {
        return internalBuffer.setZero(index, length);
    }

    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return internalBuffer.setCharSequence(index, sequence, charset);
    }

    public boolean readBoolean() {
        return internalBuffer.readBoolean();
    }

    public byte readByte() {
        return internalBuffer.readByte();
    }

    public short readUnsignedByte() {
        return internalBuffer.readUnsignedByte();
    }

    public short readShort() {
        return internalBuffer.readShort();
    }

    public short readShortLE() {
        return internalBuffer.readShortLE();
    }

    public int readUnsignedShort() {
        return internalBuffer.readUnsignedShort();
    }

    public int readUnsignedShortLE() {
        return internalBuffer.readUnsignedShortLE();
    }

    public int readMedium() {
        return internalBuffer.readMedium();
    }

    public int readMediumLE() {
        return internalBuffer.readMediumLE();
    }

    public int readUnsignedMedium() {
        return internalBuffer.readUnsignedMedium();
    }

    public int readUnsignedMediumLE() {
        return internalBuffer.readUnsignedMediumLE();
    }

    public int readInt() {
        return internalBuffer.readInt();
    }

    public int readIntLE() {
        return internalBuffer.readIntLE();
    }

    public long readUnsignedInt() {
        return internalBuffer.readUnsignedInt();
    }

    public long readUnsignedIntLE() {
        return internalBuffer.readUnsignedIntLE();
    }

    public long readLong() {
        return internalBuffer.readLong();
    }

    public long readLongLE() {
        return internalBuffer.readLongLE();
    }

    public char readChar() {
        return internalBuffer.readChar();
    }

    public float readFloat() {
        return internalBuffer.readFloat();
    }

    public double readDouble() {
        return internalBuffer.readDouble();
    }

    public ByteBuf readBytes(int length) {
        return internalBuffer.readBytes(length);
    }

    public ByteBuf readSlice(int length) {
        return internalBuffer.readSlice(length);
    }

    public ByteBuf readRetainedSlice(int length) {
        return internalBuffer.readRetainedSlice(length);
    }

    public ByteBuf readBytes(ByteBuf dst) {
        return internalBuffer.readBytes(dst);
    }

    public ByteBuf readBytes(ByteBuf dst, int length) {
        return internalBuffer.readBytes(dst, length);
    }

    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return internalBuffer.readBytes(dst, dstIndex, length);
    }

    public ByteBuf readBytes(byte[] dst) {
        return internalBuffer.readBytes(dst);
    }

    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return internalBuffer.readBytes(dst, dstIndex, length);
    }

    public ByteBuf readBytes(ByteBuffer dst) {
        return internalBuffer.readBytes(dst);
    }

    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return internalBuffer.readBytes(out, length);
    }

    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return internalBuffer.readBytes(out, length);
    }

    public CharSequence readCharSequence(int length, Charset charset) {
        return internalBuffer.readCharSequence(length, charset);
    }

    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return internalBuffer.readBytes(out, position, length);
    }

    public ByteBuf skipBytes(int length) {
        return internalBuffer.skipBytes(length);
    }

    public ByteBuf writeBoolean(boolean value) {
        return internalBuffer.writeBoolean(value);
    }

    public ByteBuf writeByte(int value) {
        return internalBuffer.writeByte(value);
    }

    public ByteBuf writeShort(int value) {
        return internalBuffer.writeShort(value);
    }

    public ByteBuf writeShortLE(int value) {
        return internalBuffer.writeShortLE(value);
    }

    public ByteBuf writeMedium(int value) {
        return internalBuffer.writeMedium(value);
    }

    public ByteBuf writeMediumLE(int value) {
        return internalBuffer.writeMediumLE(value);
    }

    public ByteBuf writeInt(int value) {
        return internalBuffer.writeInt(value);
    }

    public ByteBuf writeIntLE(int value) {
        return internalBuffer.writeIntLE(value);
    }

    public ByteBuf writeLong(long value) {
        return internalBuffer.writeLong(value);
    }

    public ByteBuf writeLongLE(long value) {
        return internalBuffer.writeLongLE(value);
    }

    public ByteBuf writeChar(int value) {
        return internalBuffer.writeChar(value);
    }

    public ByteBuf writeFloat(float value) {
        return internalBuffer.writeFloat(value);
    }

    public ByteBuf writeDouble(double value) {
        return internalBuffer.writeDouble(value);
    }

    public ByteBuf writeBytes(ByteBuf src) {
        return internalBuffer.writeBytes(src);
    }

    public ByteBuf writeBytes(ByteBuf src, int length) {
        return internalBuffer.writeBytes(src, length);
    }

    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return internalBuffer.writeBytes(src, srcIndex, length);
    }

    public ByteBuf writeBytes(byte[] src) {
        return internalBuffer.writeBytes(src);
    }

    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return internalBuffer.writeBytes(src, srcIndex, length);
    }

    public ByteBuf writeBytes(ByteBuffer src) {
        return internalBuffer.writeBytes(src);
    }

    public int writeBytes(InputStream in, int length) throws IOException {
        return internalBuffer.writeBytes(in, length);
    }

    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return internalBuffer.writeBytes(in, length);
    }

    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return internalBuffer.writeBytes(in, position, length);
    }

    public ByteBuf writeZero(int length) {
        return internalBuffer.writeZero(length);
    }

    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return internalBuffer.writeCharSequence(sequence, charset);
    }

    public int indexOf(int fromIndex, int toIndex, byte value) {
        return internalBuffer.indexOf(fromIndex, toIndex, value);
    }

    public int bytesBefore(byte value) {
        return internalBuffer.bytesBefore(value);
    }

    public int bytesBefore(int length, byte value) {
        return internalBuffer.bytesBefore(length, value);
    }

    public int bytesBefore(int index, int length, byte value) {
        return internalBuffer.bytesBefore(index, length, value);
    }

    public int forEachByte(ByteProcessor processor) {
        return internalBuffer.forEachByte(processor);
    }

    public int forEachByte(int index, int length, ByteProcessor processor) {
        return internalBuffer.forEachByte(index, length, processor);
    }

    public int forEachByteDesc(ByteProcessor processor) {
        return internalBuffer.forEachByteDesc(processor);
    }

    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return internalBuffer.forEachByteDesc(index, length, processor);
    }

    public ByteBuf copy() {
        return internalBuffer.copy();
    }

    public ByteBuf copy(int index, int length) {
        return internalBuffer.copy(index, length);
    }

    public ByteBuf slice() {
        return internalBuffer.slice();
    }

    public ByteBuf retainedSlice() {
        return internalBuffer.retainedSlice();
    }

    public ByteBuf slice(int index, int length) {
        return internalBuffer.slice(index, length);
    }

    public ByteBuf retainedSlice(int index, int length) {
        return internalBuffer.retainedSlice(index, length);
    }

    public ByteBuf duplicate() {
        return internalBuffer.duplicate();
    }

    public ByteBuf retainedDuplicate() {
        return internalBuffer.retainedDuplicate();
    }

    public int nioBufferCount() {
        return internalBuffer.nioBufferCount();
    }

    public ByteBuffer nioBuffer() {
        return internalBuffer.nioBuffer();
    }

    public ByteBuffer nioBuffer(int index, int length) {
        return internalBuffer.nioBuffer(index, length);
    }

    public ByteBuffer internalNioBuffer(int index, int length) {
        return internalBuffer.internalNioBuffer(index, length);
    }

    public ByteBuffer[] nioBuffers() {
        return internalBuffer.nioBuffers();
    }

    public ByteBuffer[] nioBuffers(int index, int length) {
        return internalBuffer.nioBuffers(index, length);
    }

    public boolean hasArray() {
        return internalBuffer.hasArray();
    }

    public byte[] array() {
        return internalBuffer.array();
    }

    public int arrayOffset() {
        return internalBuffer.arrayOffset();
    }

    public boolean hasMemoryAddress() {
        return internalBuffer.hasMemoryAddress();
    }

    public long memoryAddress() {
        return internalBuffer.memoryAddress();
    }

    public String toString(Charset charset) {
        return internalBuffer.toString(charset);
    }

    public String toString(int index, int length, Charset charset) {
        return internalBuffer.toString(index, length, charset);
    }

    public int hashCode() {
        return internalBuffer.hashCode();
    }

    public boolean equals(Object obj) {
        return (obj.getClass().equals(PacketBuffer.class)) && internalBuffer.equals(obj);
    }

    public int compareTo(ByteBuf buffer) {
        return internalBuffer.compareTo(buffer);
    }

    public String toString() {
        return internalBuffer.toString();
    }

    public ByteBuf retain(int increment) {
        return internalBuffer.retain(increment);
    }

    public int refCnt() {
        return internalBuffer.refCnt();
    }

    public ByteBuf retain() {
        return internalBuffer.retain();
    }

    public ByteBuf touch() {
        return internalBuffer.touch();
    }

    public ByteBuf touch(Object hint) {
        return internalBuffer.touch(hint);
    }

    public boolean release() {
        return internalBuffer.release();
    }

    public boolean release(int i) {
        return internalBuffer.release(i);
    }

}
