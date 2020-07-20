package com.ywh.olrn.executor;


import static com.ywh.olrn.util.TypeUtils.*;

/**
 * 字节码修改工具
 * CONSTANT_Class_info: [ tag ] [ name_index ]，其中 tag == 7，name_index 指向 CONSTANT_Utf8_info，共 1 + 2 位
 * CONSTANT_Utf8_info：[ tag ] [ 当前常量的长度 ] [ 常量的符号引用的字符串值 ]，其中 tag == 1，共 1 + 2 + n 位
 * 目标是修改值为 java/lang/System 的 CONSTANT_Utf8_info，替换为 CustomSystem
 *
 * @author ywh
 * @since 12/07/2020
 */
public class ClassModifier {

    /**
     * 常量池起始偏移：魔数（4bytes，0xCAFEBABE）和版本号（4bytes，次版本号 + 主版本号）
     */
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;

    /**
     * CONSTANT_UTF8_INFO 常量 tag
     */
    private static final int CONSTANT_UTF8_INFO = 1;

    /**
     * 常量池中 11 种常量的长度，CONSTANT_ITEM_LENGTH[tag] 表示它的长度
     */
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};

    /**
     * 1 个和 2 个字节的符号数，用来在 classByte 数组中取 tag 和 len
     * tag 用 u1 个字节表示
     * len 用 u2 个字节表示
     */
    private static final int U1 = 1;

    private static final int U2 = 2;

    public byte[] getByteCode() {
        return byteCode;
    }

    private byte[] byteCode;

    public ClassModifier(byte[] byteCode) {
        this.byteCode = byteCode;
    }

    /**
     * 从 0x00000008 开始向后取2个字节，表示池中常量的个数
     *
     * @return
     */
    public int getConstantPoolCount() {
        return bytes2Int(byteCode, CONSTANT_POOL_COUNT_INDEX, U2);
    }

    /**
     * 字节码修改器
     *
     * @param oldStr
     * @param newStr
     */
    public void modifyUTF8Constant(String oldStr, String newStr) {
        // 注意常量数量计数器从 1 开始
        int cpc = getConstantPoolCount();
        // 真实的常量起始位置
        int offset = CONSTANT_POOL_COUNT_INDEX + U2;

        // 遍历所有常量，找到 CONSTANT_UTF8_INFO
        for (int i = 1; i < cpc; i++) {

            // 找到当前常量的 tag 值
            int tag = bytes2Int(byteCode, offset, U1);

            // 根据 tag 值判断，如果当前为 CONSTANT_UTF8_INFO，则判断是否为 oldStr
            if (tag == CONSTANT_UTF8_INFO) {
                int len = bytes2Int(byteCode, offset + U1, U2);
                
                // 偏移量添加 u1（tag）、u2（常量长度）
                offset += U1 + U2;
                String str = byte2String(byteCode, offset, len);
                if (str.equals(oldStr)) {
                    byte[] strReplaceBytes = string2Byte(newStr);
                    byte[] intReplaceBytes = int2Byte(strReplaceBytes.length, U2);
                    // 替换新的字符串的长度
                    byteCode = byteReplace(byteCode, offset - U2, U2, intReplaceBytes);
                    // 替换字符串本身
                    byteCode = byteReplace(byteCode, offset, len, strReplaceBytes);
                    return;
                } else {
                    offset += len;
                }
            } else {
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
    }

}
