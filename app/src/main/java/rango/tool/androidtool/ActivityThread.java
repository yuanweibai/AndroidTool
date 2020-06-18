package rango.tool.androidtool;


public class ActivityThread {


    private static int mFlags;

    public static void main(String[] args) throws Exception {

        String string = "၀.၀၂၅၀၀";

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        double temp = Double.parseDouble(unicode.toString());
        System.out.println("double = " + temp + ", unicode = " + unicode);


    }

    static void setFlags(int flags, int mask) {
        mFlags = (mFlags & ~mask) | (flags & mask);
    }
}
