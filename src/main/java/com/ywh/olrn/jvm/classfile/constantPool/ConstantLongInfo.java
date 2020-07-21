package com.ywh.olrn.jvm.classfile.constantPool;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 长整型常量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantLongInfo implements ConstantInfo {

    private long value;

    public ConstantLongInfo(ClassReader reader) {
        value = reader.next2U4ToLong(); //u4
    }

    @Override
    public String getValue() {
        return value + "";
    }

    @Override
    public String toString() {
        return "Long: " + value;
    }

}
