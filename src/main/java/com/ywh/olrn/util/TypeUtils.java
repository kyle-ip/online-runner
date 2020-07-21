package com.ywh.olrn.util;


/**
 * 类型转换工具类
 *
 * @author ywh
 * @since 20/07/2020
 */
public class TypeUtils {

    /**
     * byte[] 转 int，byte 的范围是 [-128, 127]，此处视为无符号 [0, 255]
     *
     * @param bytes
     * @return
     */
    static public int byteArr2Int(byte[] bytes) {
        int size = bytes.length;
        /*bytes[0]为高位*/
        int res = 0xff & bytes[0];
        for (int i = 1; i < size; i++) {
            //res = ((res << 8) + 0xff) & bytes[i];
            res = (res << 8) + (0xff & bytes[i]);
        }
        return res;
    }

    /**
     * @param bytes
     * @return
     */
    static public long byteArr2Long(byte[] bytes) {
        int size = bytes.length;
        long res = 0xff & bytes[0];
        for (int i = 1; i < size; i++) {
            //res = ((res << 8) + (2 << (8 * i) - 1)) & bytes[i];
            res = (res << 8) + (bytes[i] & 0xff);
        }
        return res;
    }

    /**
     * byte 转 int，byte 的范围是 [-128, 127]，此处视为无符号 [0, 255]
     *
     * @param value
     * @return
     */
    static public int bytesInt(byte value) {
        return 0xff & value;
    }

    /**
     * @param b
     * @param start
     * @param len
     * @return
     */
    public static int bytes2Int(byte[] b, int start, int len) {
        int res = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int cur = ((int) b[i]) & 0xff;
            cur <<= (--len) * 8;
            res += cur;
        }
        return res;
    }

    /**
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;
        return int1 | int2 | int3 | int4;
    }

    /**
     * @param bytes
     * @return
     */
    static public float byteArr2Float(byte[] bytes) {
        return Float.intBitsToFloat(byteArr2Int(bytes));
    }

    /**
     * @param bytes
     * @return
     */
    static public double byteArr2Double(byte[] bytes) {
        return Double.longBitsToDouble(byteArr2Long(bytes));
    }

    /**
     * @param value
     * @return
     */
    static public int float2Int(float value) {
        return Float.floatToIntBits(value);
    }

    /**
     * @param value
     * @return
     */
    static public float int2Float(int value) {
        return Float.intBitsToFloat(value);
    }

    /**
     * byte[] 转 String
     *
     * @param bytes
     * @return
     */
    public static String byte2String(byte[] bytes) {
        int size = bytes.length;
        char[] res = new char[size];
        for (int i = 0; i < size; i++) {
            res[i] = (char) bytes[i];
        }
        return String.valueOf(res);
    }

    /**
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte b : bytes) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * @param b
     * @param start
     * @param len
     * @return
     */
    public static String byte2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }


    /**
     * @param str
     * @return
     */
    public static byte[] string2Byte(String str) {
        return str.getBytes();
    }

//    /**
//     * U1[] to String
//     *
//     * @param bytes
//     * @return
//     */
//    static public String u12String(U1[] bytes) {
//        int size = bytes.length;
//        char[] res = new char[size];
//        for (int i = 0; i < size; i++) {
//            res[i] = (char) bytes[i].u1[0];
//        }
//        return String.valueOf(res);
//    }

    /**
     * @param s
     * @param d
     * @return
     */
    static public boolean compare(String s, String d) {
        if (s == null && d == null) {
            return true;
        }
        if (s == null || d == null) {
            return false;
        }
        Integer len0 = s.length();
        Integer len1 = d.length();
        if (!len0.equals(len1)) {
            return false;
        }

        char[] arr0 = s.toCharArray();
        char[] arr1 = d.toCharArray();
        for (int i = 0; i < len0; i++) {
            if (arr0[i] != arr1[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param high
     * @param low
     * @return
     */
    public static byte[] appendByte(byte[] high, byte[] low) {
        int len = high.length + low.length;
        byte[] res = new byte[len];
        int i = 0;

        for (int highLen = high.length; i < highLen; i++) {
            res[i] = high[i];
        }
        for (int j = 0, lowLen = high.length; j < lowLen; j++) {
            res[i++] = low[j];
        }
        return res;
    }


    /**
     * @param oldBytes
     * @param offset
     * @param len
     * @param replaceBytes
     * @return
     */
    public static byte[] byteReplace(byte[] oldBytes, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[oldBytes.length + replaceBytes.length - len];
        System.arraycopy(oldBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(oldBytes, offset + len, newBytes, offset + replaceBytes.length,
            oldBytes.length - offset - len);
        return newBytes;
    }


    /**
     * @param num
     * @param len
     * @return
     */
    public static byte[] int2Byte(int num, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((num >> (8 * i)) & 0xff);
        }
        return b;
    }
}