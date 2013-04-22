package com.geargames.common.util;

/**
 * User: abarakov
 * Date: 22.04.13
 * Выдержка из класса java.util.Arrays
 */
public class ArrayUtils {

    /**
     * Check that fromIndex and toIndex are in range, and throw an appropriate exception if they aren't.
     */
    private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > arrayLen) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(byte[] a, byte[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(short[] a, short[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(int[] a, int[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(long[] a, long[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(char[] a, char[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(float[] a, float[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) { // Float.floatToIntBits(a[i])
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(double[] a, double[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) { // Double.doubleToLongBits(a[i])
                return false;
            }
        }
        return true;
    }

    /**
     * Вернет true, если два аргумента ссылаются на один и тот же массив или два массива имеют одинаковую длину и
     * каждая пара соответствующих элементов имеют одинаковое значение. В противном случае вернет false.
     */
    public static boolean equals(Object[] a, Object[] a2) {
        if (a == a2) {
            return true;
        }
        if (a == null || a2 == null) {
            return false;
        }
        int length = a.length;
        if (a2.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            Object o1 = a[i];
            Object o2 = a2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(byte[] a, byte value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(byte[] a, int fromIndex, int toIndex, byte value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(short[] a, short value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(short[] a, int fromIndex, int toIndex, short value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(int[] a, int value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(int[] a, int fromIndex, int toIndex, int value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(long[] a, long value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(long[] a, int fromIndex, int toIndex, long value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(char[] a, char value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(char[] a, int fromIndex, int toIndex, char value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(float[] a, float value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(float[] a, int fromIndex, int toIndex, float value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(double[] a, double value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(double[] a, int fromIndex, int toIndex, double value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает каждому элементу массива значение value.
     */
    public static void fill(Object[] a, Object value) {
        for (int i = 0; i < a.length; i++) {
            a[i] = value;
        }
    }

    /**
     * Присваивает диапазону массива [fromIndex, toIndex) значение value.
     */
    public static void fill(Object[] a, int fromIndex, int toIndex, Object value) {
        rangeCheck(a.length, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            a[i] = value;
        }
    }

    /**
     * Копирует массив. Копируемый массив будет длинной newLength, если newLength больше чем длинна исходного массива,
     * новые элементы массива заполняются нулями.
     */
    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static short[] copyOf(short[] original, int newLength) {
        short[] copy = new short[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static int[] copyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static long[] copyOf(long[] original, int newLength) {
        long[] copy = new long[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static char[] copyOf(char[] original, int newLength) {
        char[] copy = new char[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static float[] copyOf(float[] original, int newLength) {
        float[] copy = new float[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static double[] copyOf(double[] original, int newLength) {
        double[] copy = new double[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = 0;
        }
        return copy;
    }

    public static boolean[] copyOf(boolean[] original, int newLength) {
        boolean[] copy = new boolean[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = false;
        }
        return copy;
    }

    public static Object[] copyOf(Object[] original, int newLength) {
        Object[] copy = new Object[newLength];
        int minLength = Mathematics.min(original.length, newLength);
        for (int i = 0; i < minLength; i++) {
            copy[i] = original[i];
        }
        for (int i = minLength; i < newLength; i++) {
            copy[i] = null;
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static byte[] copyOfRange(byte[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        byte[] copy = new byte[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static short[] copyOfRange(short[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        short[] copy = new short[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static int[] copyOfRange(int[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        int[] copy = new int[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static long[] copyOfRange(long[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        long[] copy = new long[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static char[] copyOfRange(char[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        char[] copy = new char[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static float[] copyOfRange(float[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        float[] copy = new float[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static double[] copyOfRange(double[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        double[] copy = new double[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static boolean[] copyOfRange(boolean[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        boolean[] copy = new boolean[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

    /**
     * Копирует диапазон массива, как новый массив.
     * @param original  исходный массив, часть элементов которого, будут скопированы в новый массив.
     * @param fromIndex индекс первого элемента массива original (включительно)
     * @param toIndex   индекс последнего элемента массива original (не включительно)
     * @return
     */
    public static Object[] copyOfRange(Object[] original, int fromIndex, int toIndex) {
        rangeCheck(original.length, fromIndex, toIndex);
        int newLength = toIndex - fromIndex;
        Object[] copy = new Object[newLength];
        for (int i = fromIndex; i < toIndex; i++) {
            copy[i - fromIndex] = original[i];
        }
        return copy;
    }

}
