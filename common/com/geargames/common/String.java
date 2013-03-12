package com.geargames.common;

public class String {

    public final static char SPACE = ' ';
    public final static char LINE_SEPARATOR = '\n';

    private StringBuilder string;

    private String(java.lang.String str) {
        this.string = new StringBuilder(str);
    }

    private String(StringBuilder stringBuilder) {
        string = new StringBuilder(stringBuilder);
    }


    public static String valueOf(String str) {
        return new String(str.string);
    }

    public static String valueOfC(java.lang.String str) {
        return new String(str);
    }

    public static String valueOfI(int value) {
        return new String(java.lang.String.valueOf(value));
    }


    public String concat(Object obj) {
        return new String(string.append(obj));
    }

    public String concatC(java.lang.String str) {
        return new String(string.append(str));
    }

    public String concatCh(char value) {
        return new String(string.append(value));
    }

    public String concatI(int value) {
        return new String(string.append(value));
    }

    public String concatL(long value) {
        return new String(string.append(value));
    }

    public String concatB(boolean value) {
        return new String(string.append(value));
    }

    public char charAt(int index) {
        return string.charAt(index);
    }

    public String substring(int start, int end) {
        return new String(string.substring(start, end));
    }

    public String substring(int start) {
        return new String(string.substring(start));
    }

    public int length() {
        return string.length();
    }

    public String replace(String target, String replacement) {
        int start = string.indexOf(target.toString());
        if (start == -1) {//нет искомого
            return new String(string);//String.valueOf(this);//new String(string);
        } else {
            return new String(new StringBuilder(string).replace(start, start + target.length(), replacement.toString()));
        }
    }


    public int indexOf(char character) {
        return string.indexOf(java.lang.String.valueOf(character));
    }

    public int indexOf(char character, int from) {
        return string.indexOf(java.lang.String.valueOf(character), from);
    }

    public int indexOf(String str, int from) {
        return string.indexOf(str.toString(), from);
    }

    public boolean equals(String str) {
        return this.toString().equals(str.toString());
    }

    public boolean contains(String str) {
        return string.indexOf(str.toString()) > -1;
    }

    public java.lang.String toString() {
        return string.toString();
    }
}
