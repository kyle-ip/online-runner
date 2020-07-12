package com.ywh.olrn.util;

/**
 * byte 转换工具
 *
 * @author ywh
 * @since 12/07/2020
 */
public class ByteUtil {

    /**
     *
     * @param b
     * @param start
     * @param len
     * @return
     */
    public static int byte2Int(byte[] b, int start, int len) {
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
     *
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

    /**
     *
     * @param b
     * @param start
     * @param len
     * @return
     */
    public static String byte2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }

    /**
     *
     * @param str
     * @return
     */
    public static byte[] string2Byte(String str) {
        return str.getBytes();
    }

    /**
     *
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
        System.arraycopy(oldBytes, offset + len, newBytes, offset + replaceBytes.length, oldBytes.length - offset - len);
        return newBytes;
    }
}
