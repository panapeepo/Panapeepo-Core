/*
 * MIT License
 *
 * Copyright (c) 2020 Panapeepo (https://github.com/Panapeepo)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.panapeepo.api.database.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ThreadSaveGrowingByteBuffer extends ByteBuf {

    public ThreadSaveGrowingByteBuffer(ByteBuf parent) {
        this.parent = parent;
    }

    private final ByteBuf parent;

    @Override
    public int capacity() {
        return parent.capacity();
    }

    @Override
    public ByteBuf capacity(int i) {
        return parent.capacity(i);
    }

    @Override
    public int maxCapacity() {
        return parent.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return parent.alloc();
    }

    @Override
    @Deprecated
    public ByteOrder order() {
        return parent.order();
    }

    @Override
    @Deprecated
    public ByteBuf order(ByteOrder byteOrder) {
        return parent.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return parent.unwrap();
    }

    @Override
    public boolean isDirect() {
        return parent.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public ByteBuf asReadOnly() {
        return this;
    }

    @Override
    public int readerIndex() {
        return parent.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int i) {
        return parent.readerIndex(i);
    }

    @Override
    public int writerIndex() {
        return parent.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int i) {
        return parent.writerIndex(i);
    }

    @Override
    public ByteBuf setIndex(int i, int i1) {
        return parent.setIndex(i, i1);
    }

    @Override
    public int readableBytes() {
        return parent.readableBytes();
    }

    @Override
    public int writableBytes() {
        return parent.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return parent.maxWritableBytes();
    }

    @Override
    public int maxFastWritableBytes() {
        return parent.maxFastWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return parent.isReadable();
    }

    @Override
    public boolean isReadable(int i) {
        return parent.isReadable(i);
    }

    @Override
    public boolean isWritable() {
        return parent.isWritable();
    }

    @Override
    public boolean isWritable(int i) {
        return parent.isWritable(i);
    }

    @Override
    public ByteBuf clear() {
        return parent.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return parent.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return parent.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return parent.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return parent.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return parent.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return parent.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int i) {
        return parent.ensureWritable(i);
    }

    @Override
    public int ensureWritable(int i, boolean b) {
        return parent.ensureWritable(i, b);
    }

    @Override
    public boolean getBoolean(int i) {
        return parent.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return parent.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return parent.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return parent.getShort(i);
    }

    @Override
    public short getShortLE(int i) {
        return parent.getShortLE(i);
    }

    @Override
    public int getUnsignedShort(int i) {
        return parent.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int i) {
        return parent.getUnsignedShortLE(i);
    }

    @Override
    public int getMedium(int i) {
        return parent.getMedium(i);
    }

    @Override
    public int getMediumLE(int i) {
        return parent.getMediumLE(i);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return parent.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int i) {
        return parent.getUnsignedMediumLE(i);
    }

    @Override
    public int getInt(int i) {
        return parent.getInt(i);
    }

    @Override
    public int getIntLE(int i) {
        return parent.getIntLE(i);
    }

    @Override
    public long getUnsignedInt(int i) {
        return parent.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int i) {
        return parent.getUnsignedIntLE(i);
    }

    @Override
    public long getLong(int i) {
        return parent.getLong(i);
    }

    @Override
    public long getLongLE(int i) {
        return parent.getLongLE(i);
    }

    @Override
    public char getChar(int i) {
        return parent.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return parent.getFloat(i);
    }

    @Override
    public float getFloatLE(int index) {
        return parent.getFloatLE(index);
    }

    @Override
    public double getDouble(int i) {
        return parent.getDouble(i);
    }

    @Override
    public double getDoubleLE(int index) {
        return parent.getDoubleLE(index);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf) {
        return parent.getBytes(i, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1) {
        return parent.getBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return parent.getBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes) {
        return parent.getBytes(i, bytes);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes, int i1, int i2) {
        return parent.getBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuffer byteBuffer) {
        return parent.getBytes(i, byteBuffer);
    }

    @Override
    public ByteBuf getBytes(int i, OutputStream outputStream, int i1) throws IOException {
        return parent.getBytes(i, outputStream, i1);
    }

    @Override
    public int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i1) throws IOException {
        return parent.getBytes(i, gatheringByteChannel, i1);
    }

    @Override
    public int getBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return parent.getBytes(i, fileChannel, l, i1);
    }

    @Override
    public CharSequence getCharSequence(int i, int i1, Charset charset) {
        return parent.getCharSequence(i, i1, charset);
    }

    @Override
    public ByteBuf setBoolean(int i, boolean b) {
        return parent.setBoolean(i, b);
    }

    @Override
    public ByteBuf setByte(int i, int i1) {
        return parent.setByte(i, i1);
    }

    @Override
    public ByteBuf setShort(int i, int i1) {
        return parent.setShort(i, i1);
    }

    @Override
    public ByteBuf setShortLE(int i, int i1) {
        return parent.setShortLE(i, i1);
    }

    @Override
    public ByteBuf setMedium(int i, int i1) {
        return parent.setMedium(i, i1);
    }

    @Override
    public ByteBuf setMediumLE(int i, int i1) {
        return parent.setMediumLE(i, i1);
    }

    @Override
    public ByteBuf setInt(int i, int i1) {
        return parent.setInt(i, i1);
    }

    @Override
    public ByteBuf setIntLE(int i, int i1) {
        return parent.setIntLE(i, i1);
    }

    @Override
    public ByteBuf setLong(int i, long l) {
        return parent.setLong(i, l);
    }

    @Override
    public ByteBuf setLongLE(int i, long l) {
        return parent.setLongLE(i, l);
    }

    @Override
    public ByteBuf setChar(int i, int i1) {
        return parent.setChar(i, i1);
    }

    @Override
    public ByteBuf setFloat(int i, float v) {
        return parent.setFloat(i, v);
    }

    @Override
    public ByteBuf setFloatLE(int index, float value) {
        return parent.setFloatLE(index, value);
    }

    @Override
    public ByteBuf setDouble(int i, double v) {
        return parent.setDouble(i, v);
    }

    @Override
    public ByteBuf setDoubleLE(int index, double value) {
        return parent.setDoubleLE(index, value);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf) {
        return parent.setBytes(i, byteBuf);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1) {
        return parent.setBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return parent.setBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes) {
        return parent.setBytes(i, bytes);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes, int i1, int i2) {
        return parent.setBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuffer byteBuffer) {
        return parent.setBytes(i, byteBuffer);
    }

    @Override
    public int setBytes(int i, InputStream inputStream, int i1) throws IOException {
        return parent.setBytes(i, inputStream, i1);
    }

    @Override
    public int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i1) throws IOException {
        return parent.setBytes(i, scatteringByteChannel, i1);
    }

    @Override
    public int setBytes(int i, FileChannel fileChannel, long l, int i1) throws IOException {
        return parent.setBytes(i, fileChannel, l, i1);
    }

    @Override
    public ByteBuf setZero(int i, int i1) {
        return parent.setZero(i, i1);
    }

    @Override
    public int setCharSequence(int i, CharSequence charSequence, Charset charset) {
        return parent.setCharSequence(i, charSequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return this.readSecurely(ByteBuf::readBoolean, Boolean.FALSE);
    }

    @Override
    public byte readByte() {
        return this.readSecurely(ByteBuf::readByte, (byte) 0);
    }

    @Override
    public short readUnsignedByte() {
        return this.readSecurely(ByteBuf::readUnsignedByte, (short) 0);
    }

    @Override
    public short readShort() {
        return this.readSecurely(ByteBuf::readShort, (short) 0);
    }

    @Override
    public short readShortLE() {
        return this.readSecurely(ByteBuf::readShortLE, (short) 0);
    }

    @Override
    public int readUnsignedShort() {
        return this.readSecurely(ByteBuf::readUnsignedShort, 0);
    }

    @Override
    public int readUnsignedShortLE() {
        return this.readSecurely(ByteBuf::readUnsignedShortLE, 0);
    }

    @Override
    public int readMedium() {
        return this.readSecurely(ByteBuf::readMedium, 0);
    }

    @Override
    public int readMediumLE() {
        return this.readSecurely(ByteBuf::readMediumLE, 0);
    }

    @Override
    public int readUnsignedMedium() {
        return this.readSecurely(ByteBuf::readUnsignedMedium, 0);
    }

    @Override
    public int readUnsignedMediumLE() {
        return this.readSecurely(ByteBuf::readUnsignedMediumLE, 0);
    }

    @Override
    public int readInt() {
        return this.readSecurely(ByteBuf::readInt, 0);
    }

    @Override
    public int readIntLE() {
        return this.readSecurely(ByteBuf::readIntLE, 0);
    }

    @Override
    public long readUnsignedInt() {
        return this.readSecurely(ByteBuf::readUnsignedInt, 0L);
    }

    @Override
    public long readUnsignedIntLE() {
        return this.readSecurely(ByteBuf::readUnsignedIntLE, 0L);
    }

    @Override
    public long readLong() {
        return this.readSecurely(ByteBuf::readLong, 0L);
    }

    @Override
    public long readLongLE() {
        return this.readSecurely(ByteBuf::readLongLE, 0L);
    }

    @Override
    public char readChar() {
        return this.readSecurely(ByteBuf::readChar, 'a');
    }

    @Override
    public float readFloat() {
        return this.readSecurely(ByteBuf::readFloat, 0F);
    }

    @Override
    public float readFloatLE() {
        return this.readSecurely(ByteBuf::readFloatLE, 0F);
    }

    @Override
    public double readDouble() {
        return this.readSecurely(ByteBuf::readDouble, 0D);
    }

    @Override
    public double readDoubleLE() {
        return this.readSecurely(ByteBuf::readDoubleLE, 0D);
    }

    public <T> T readSecurely(Function<ByteBuf, T> function, T defaultValue) {
        try {
            return function.apply(this.parent);
        } catch (Throwable any) {
            return defaultValue;
        }
    }

    @Override
    public ByteBuf readBytes(int i) {
        return parent.readBytes(i);
    }

    @Override
    public ByteBuf readSlice(int i) {
        return parent.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int i) {
        return parent.readRetainedSlice(i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        return parent.readBytes(byteBuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i) {
        return parent.readBytes(byteBuf, i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i, int i1) {
        return parent.readBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes) {
        return parent.readBytes(bytes);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes, int i, int i1) {
        return parent.readBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        return parent.readBytes(byteBuffer);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int i) throws IOException {
        return parent.readBytes(outputStream, i);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException {
        return parent.readBytes(gatheringByteChannel, i);
    }

    @Override
    public CharSequence readCharSequence(int i, Charset charset) {
        return parent.readCharSequence(i, charset);
    }

    @Override
    public int readBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return parent.readBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf skipBytes(int i) {
        return parent.skipBytes(i);
    }

    @Override
    public ByteBuf writeBoolean(boolean b) {
        return parent.writeBoolean(b);
    }

    @Override
    public ByteBuf writeByte(int i) {
        return parent.writeByte(i);
    }

    @Override
    public ByteBuf writeShort(int i) {
        return parent.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int i) {
        return parent.writeShortLE(i);
    }

    @Override
    public ByteBuf writeMedium(int i) {
        return parent.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int i) {
        return parent.writeMediumLE(i);
    }

    @Override
    public ByteBuf writeInt(int i) {
        return parent.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int i) {
        return parent.writeIntLE(i);
    }

    @Override
    public ByteBuf writeLong(long l) {
        return parent.writeLong(l);
    }

    @Override
    public ByteBuf writeLongLE(long l) {
        return parent.writeLongLE(l);
    }

    @Override
    public ByteBuf writeChar(int i) {
        return parent.writeChar(i);
    }

    @Override
    public ByteBuf writeFloat(float v) {
        return parent.writeFloat(v);
    }

    @Override
    public ByteBuf writeFloatLE(float value) {
        return parent.writeFloatLE(value);
    }

    @Override
    public ByteBuf writeDouble(double v) {
        return parent.writeDouble(v);
    }

    @Override
    public ByteBuf writeDoubleLE(double value) {
        return parent.writeDoubleLE(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        return parent.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i) {
        return parent.writeBytes(byteBuf, i);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i, int i1) {
        return parent.writeBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes) {
        return parent.writeBytes(bytes);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes, int i, int i1) {
        return parent.writeBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        return parent.writeBytes(byteBuffer);
    }

    @Override
    public int writeBytes(InputStream inputStream, int i) throws IOException {
        return parent.writeBytes(inputStream, i);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException {
        return parent.writeBytes(scatteringByteChannel, i);
    }

    @Override
    public int writeBytes(FileChannel fileChannel, long l, int i) throws IOException {
        return parent.writeBytes(fileChannel, l, i);
    }

    @Override
    public ByteBuf writeZero(int i) {
        return parent.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence charSequence, Charset charset) {
        return parent.writeCharSequence(charSequence, charset);
    }

    @Override
    public int indexOf(int i, int i1, byte b) {
        return parent.indexOf(i, i1, b);
    }

    @Override
    public int bytesBefore(byte b) {
        return parent.bytesBefore(b);
    }

    @Override
    public int bytesBefore(int i, byte b) {
        return parent.bytesBefore(i, b);
    }

    @Override
    public int bytesBefore(int i, int i1, byte b) {
        return parent.bytesBefore(i, i1, b);
    }

    @Override
    public int forEachByte(ByteProcessor byteProcessor) {
        return parent.forEachByte(byteProcessor);
    }

    @Override
    public int forEachByte(int i, int i1, ByteProcessor byteProcessor) {
        return parent.forEachByte(i, i1, byteProcessor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor byteProcessor) {
        return parent.forEachByteDesc(byteProcessor);
    }

    @Override
    public int forEachByteDesc(int i, int i1, ByteProcessor byteProcessor) {
        return parent.forEachByteDesc(i, i1, byteProcessor);
    }

    @Override
    public ByteBuf copy() {
        return parent.copy();
    }

    @Override
    public ByteBuf copy(int i, int i1) {
        return parent.copy(i, i1);
    }

    @Override
    public ByteBuf slice() {
        return parent.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return parent.retainedSlice();
    }

    @Override
    public ByteBuf slice(int i, int i1) {
        return parent.slice(i, i1);
    }

    @Override
    public ByteBuf retainedSlice(int i, int i1) {
        return parent.retainedSlice(i, i1);
    }

    @Override
    public ByteBuf duplicate() {
        return parent.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return parent.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return parent.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return parent.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int i, int i1) {
        return parent.nioBuffer(i, i1);
    }

    @Override
    public ByteBuffer internalNioBuffer(int i, int i1) {
        return parent.internalNioBuffer(i, i1);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return parent.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int i, int i1) {
        return parent.nioBuffers(i, i1);
    }

    @Override
    public boolean hasArray() {
        return parent.hasArray();
    }

    @Override
    public byte[] array() {
        return parent.array();
    }

    @Override
    public int arrayOffset() {
        return parent.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return parent.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return parent.memoryAddress();
    }

    @Override
    public boolean isContiguous() {
        return parent.isContiguous();
    }

    @Override
    public String toString(Charset charset) {
        return parent.toString(charset);
    }

    @Override
    public String toString(int i, int i1, Charset charset) {
        return parent.toString(i, i1, charset);
    }

    @Override
    public int hashCode() {
        return parent.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return parent.equals(o);
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return parent.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return parent.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return parent.retain(i);
    }

    @Override
    public ByteBuf retain() {
        return parent.retain();
    }

    @Override
    public ByteBuf touch() {
        return parent.touch();
    }

    @Override
    public ByteBuf touch(Object o) {
        return parent.touch(o);
    }

    @Override
    public int refCnt() {
        return parent.refCnt();
    }

    @Override
    public boolean release() {
        return parent.release();
    }

    @Override
    public boolean release(int i) {
        return parent.release(i);
    }

    public void writeString(String stringToWrite) {
        this.save(stringToWrite, s -> {
            byte[] buffer = s.getBytes(StandardCharsets.UTF_8);
            this.writeInt(buffer.length);
            this.writeBytes(buffer);
        });
    }

    public String readString() {
        return this.read(buf -> {
            int length = buf.readInt();
            byte[] buffer = new byte[length];
            this.readBytes(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        }, null);
    }

    public void writeArray(byte[] bytes) {
        this.save(bytes, array -> {
            this.writeInt(array.length);
            this.writeBytes(array);
        });
    }

    public byte[] readArray() {
        return this.read(buf -> {
            int length = this.readInt();
            byte[] buffer = new byte[length];
            this.readBytes(buffer);
            return buffer;
        }, new byte[0]);
    }

    public byte[] toArray() {
        byte[] bytes = new byte[this.readableBytes()];
        this.readBytes(bytes);
        return bytes;
    }

    public void writeStringArray(Collection<String> list) {
        this.writeInt(list.size());
        for (String s : list) {
            this.writeString(s);
        }
    }

    public List<String> readStringArray() {
        return this.read(buf -> {
            int length = buf.readInt();
            List<String> out = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                out.add(this.readString());
            }

            return out;
        }, new ArrayList<>());
    }

    public void writeStringMap(Map<String, String> map) {
        this.writeInt(map.size());
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            this.writeString(stringStringEntry.getKey());
            this.writeString(stringStringEntry.getValue());
        }
    }

    public Map<String, String> readStringMap() {
        return this.read(buf -> {
            int length = buf.readInt();
            Map<String, String> out = new HashMap<>(length);
            for (int i = 0; i < length; i++) {
                out.put(this.readString(), this.readString());
            }

            return out;
        }, new HashMap<>());
    }

    public void writeStringArrays(String[] strings) {
        this.writeInt(strings.length);
        for (String string : strings) {
            this.writeString(string);
        }
    }

    public String[] readStringArrays() {
        return this.read(buf -> {
            int length = buf.readInt();
            String[] out = new String[length];
            for (int i = 0; i < length; i++) {
                out[i] = this.readString();
            }

            return out;
        }, new String[0]);
    }

    public void writeLongArray(long[] longs) {
        this.writeInt(longs.length);
        for (long aLong : longs) {
            this.writeLong(aLong);
        }
    }

    public long[] readLongArray() {
        return this.read(buf -> {
            int length = buf.readInt();
            long[] out = new long[length];
            for (int i = 0; i < length; i++) {
                out[i] = this.readLong();
            }

            return out;
        }, new long[0]);
    }

    public UUID readUniqueId() {
        return this.readSecurely(buf -> new UUID(this.readLong(), this.readLong()), null);
    }

    public void writeUniqueId(UUID uniqueId) {
        this.save(uniqueId, uuid -> {
            this.writeLong(uuid.getMostSignificantBits());
            this.writeLong(uuid.getLeastSignificantBits());
        });
    }

    private <T> void save(T in, Consumer<T> ifPresent) {
        this.writeBoolean(in != null);
        if (in != null) {
            ifPresent.accept(in);
        }
    }

    private <T> T read(Function<ThreadSaveGrowingByteBuffer, T> ifPresent, T defaultValue) {
        int readerIndex = this.readerIndex();

        try {
            if (this.readBoolean()) {
                return ifPresent.apply(this);
            } else {
                return defaultValue;
            }
        } catch (Throwable throwable) {
            try {
                this.readerIndex(readerIndex);
            } catch (Throwable ignored) {
            }

            return defaultValue;
        }
    }
}
