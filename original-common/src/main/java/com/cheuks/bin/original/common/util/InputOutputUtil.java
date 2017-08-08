package com.cheuks.bin.original.common.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

public class InputOutputUtil {

    private InputOutputUtil() {
        super();
    }

    final static InputOutputUtil newInstance = new InputOutputUtil();

    public static final InputOutputUtil newInstance() {
        return newInstance;
    }

    public final void writeBoolean(final OutputStream out, final boolean v) throws IOException {
        out.write(v ? 1 : 0);
    }

    public final void writeInt(final OutputStream out, final int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v >>> 0) & 0xFF);
    }

    public final byte[] writeInt(final int v) throws IOException {
        byte[] result = new byte[4];
        result[0] = (byte) ((v >>> 24) & 0xFF);
        result[1] = (byte) ((v >>> 16) & 0xFF);
        result[2] = (byte) ((v >>> 8) & 0xFF);
        result[3] = (byte) ((v >>> 0) & 0xFF);
        return result;
    }

    public final int readInt(final InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public final int readInt(final byte[] bytes) throws IOException {
        if (bytes.length < 4)
            throw new IOException("bytes length index less than 4!");
        int ch1 = bytes[0];
        int ch2 = bytes[1];
        int ch3 = bytes[2];
        int ch4 = bytes[3];
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public final void writeLong(final OutputStream out, final long v) throws IOException {
        out.write(writeLong(v), 0, 8);
    }

    public final byte[] writeLong(final long v) throws IOException {
        byte writeBuffer[] = new byte[8];
        writeBuffer[0] = (byte) (v >>> 56);
        writeBuffer[1] = (byte) (v >>> 48);
        writeBuffer[2] = (byte) (v >>> 40);
        writeBuffer[3] = (byte) (v >>> 32);
        writeBuffer[4] = (byte) (v >>> 24);
        writeBuffer[5] = (byte) (v >>> 16);
        writeBuffer[6] = (byte) (v >>> 8);
        writeBuffer[7] = (byte) (v >>> 0);
        return writeBuffer;
    }

    public final long readLong(final InputStream in) throws IOException {
        byte[] readBuffer = new byte[8];
        in.read(readBuffer, 0, 8);
        return (((long) readBuffer[0] << 56) + ((long) (readBuffer[1] & 255) << 48) + ((long) (readBuffer[2] & 255) << 40) + ((long) (readBuffer[3] & 255) << 32) + ((long) (readBuffer[4] & 255) << 24) + ((readBuffer[5] & 255) << 16) + ((readBuffer[6] & 255) << 8) + ((readBuffer[7] & 255) << 0));
    }

    public final void writeCharOrShort(final OutputStream out, final int v) throws IOException {
        out.write((v >>> 8) & 0xFF);
        out.write((v >>> 0) & 0xFF);
    }

    public final void writeByte(final OutputStream out, final int v) throws IOException {
        out.write(v);
    }

    public final void writeFloat(final OutputStream out, final float v) throws IOException {
        writeInt(out, Float.floatToIntBits(v));
    }

    public final void writeDouble(final OutputStream out, final float v) throws IOException {
        writeLong(out, Double.doubleToLongBits(v));
    }

    public final void writeBytes(final OutputStream out, final String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            out.write((byte) s.charAt(i));
        }
    }

    public int writeUTF(final OutputStream out, final String str) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");

        byte[] bytearr = new byte[utflen + 2];

        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if (!((c >= 0x0001) && (c <= 0x007F)))
                break;
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;

            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen + 2);
        return utflen + 2;
    }

    public static void main(String[] args) throws IOException {
        // Object o = ((5 >>> 16) & 0xFF);
        // System.err.println(o);

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        InputOutputUtil iou = new InputOutputUtil();
        iou.writeInt(out1, 1011);
        out2.write(iou.writeInt(1011));
        System.out.println(1);
    }

}
