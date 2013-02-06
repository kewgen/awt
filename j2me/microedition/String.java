package gg.microedition;

public class String {

    private java.lang.String string;

    public static String valueOf(String str_) {
        String nstr = new String();
        nstr.string = (str_ == null) ? "null" : str_.toString();
        return nstr;
    }

    public static String valueOfC(java.lang.String str_) {
        String nstr = new String();
        nstr.string = (str_ == null) ? "null" : str_;
        return nstr;
    }

    public static String valueOfI(int i) {
        String nstr = new String();
        nstr.string = new java.lang.String(java.lang.String.valueOf(i));
        return nstr;
    }

    public String concat(Object obj) {
        String nstr = new String();
        nstr.string = this.string.concat(obj.toString());
        return nstr;
    }

    public String concatC(java.lang.String str_) {//только для использования в непортируемых классах
        String nstr = new String();
        nstr.string = this.string.concat(str_);
        return nstr;
    }

    public String concatCh(char a) {
        String nstr = new String();
        nstr.string = this.string.concat(java.lang.String.valueOf(a));
        return nstr;
    }

    public String concatI(int a) {
        String nstr = new String();
        nstr.string = this.string.concat(java.lang.String.valueOf(a));
        return nstr;
    }

    public String concatL(long a) {
        String nstr = new String();
        nstr.string = this.string.concat(java.lang.String.valueOf(a));
        return nstr;
    }

    public char charAt(int i) {
        return string.charAt(i);
    }

    public String substring(int a, int b) {
        String nstr = new String();
        nstr.string = this.string.substring(a, b);
        return nstr;
    }

    public String substring(int a) {
        String nstr = new String();
        nstr.string = this.string.substring(a);
        return nstr;
    }

    public int length() {
        return string.length();
    }

    public String replace(String target, String replacement) {
        int from = string.indexOf(target.toString());
        String nstr = new String();
        nstr.string = (from >= 0 ? (from > 0 ? string.substring(0, from) : "").concat(replacement.toString()).concat(from + target.length() < string.length() ? string.substring(from + target.length()) : "") : string);
        return nstr;
    }

    public int indexOf(char character) {
        return string.indexOf(java.lang.String.valueOf(character));
    }

    public int indexOf(char character, int from) {
        return string.indexOf(java.lang.String.valueOf(character), from);
    }

    public boolean equals(String str) {
        return this.toString().equals(str.toString());
    }

    public java.lang.String toString() {
	    return string;
    }

}
