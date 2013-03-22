package com.geargames.common.serialization;

/**
 * User: mkutuzov
 * Date: 28.03.12
 */
public class SimpleSerializer {
    public static final byte NO = 0;
    public static final byte YES = 1;
    public static final byte ALLY = 1;
    public static final byte ENEMY = 0;

    public static final short NULL_COORDINATE = -1;

    public static final int BYTE_MASK = 0xFF;
    public static final int CHAR_MASK = 0xFFFF;
    public static final long INT_MASK = 0xFFFFFFFFL;
    public static final int DECIMAL_SCALE = 1000;



    public static void serialize(String string, MicroByteBuffer buffer) {
        serialize((short) string.length(), buffer);
        for (int i = 0; i < string.length(); i++) {
            serialize(string.charAt(i), buffer);
        }
    }

    public static void serialize(byte value, MicroByteBuffer buffer) {
        buffer.put(value);
    }

    public static void serialize(char value, MicroByteBuffer buffer) {
        buffer.put((byte) (value >>> 8));
        buffer.put((byte) (value & BYTE_MASK));
    }

    public static void serialize(short value, MicroByteBuffer buffer) {
        serialize((char) value, buffer);
    }

    public static void serialize(int value, MicroByteBuffer buffer) {
        serialize((char) (value >>> 16), buffer);
        serialize((char) (value & CHAR_MASK), buffer);
    }

    public static void serialize(long value, MicroByteBuffer buffer) {
        serialize((int) (value >>> 32), buffer);
        serialize((int) (value & INT_MASK), buffer);
    }

    public static void serialize(double value, MicroByteBuffer buffer) {
        serialize((long) value, buffer);
        serialize((long) ((value - (long) value) * DECIMAL_SCALE), buffer);
    }

    public static void serialize(float value, MicroByteBuffer buffer) {
        serialize((int) value, buffer);
        serialize((int) ((value - (int) value) * DECIMAL_SCALE), buffer);
    }

    public static void serialize(boolean value, MicroByteBuffer buffer) {
        buffer.put((byte) (value ? 1 : 0));
    }

    public static void serialize(boolean[] values, MicroByteBuffer buffer) {
        if (values.length > 8) {
            throw new IllegalArgumentException();
        }
        byte value = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i]) {
                value |= (1 << i);
            }
        }
        buffer.put((byte) values.length);
        buffer.put(value);
    }

    public static void serialize(byte[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            buffer.put(values[i]);
        }
    }

    public static void serialize(char[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

    public static void serialize(short[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

    public static void serialize(int[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

    public static void serialize(long[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

    public static void serialize(float[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

    public static void serialize(double[] values, MicroByteBuffer buffer) {
        serialize((short) values.length, buffer);
        for (int i = 0; i < values.length; i++) {
            serialize(values[i], buffer);
        }
    }

}