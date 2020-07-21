package com.ywh.olrn.jvm.classfile.constantPool;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 长浮点型常量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantDoubleInfo implements ConstantInfo {

    private Double value;

    public ConstantDoubleInfo(ClassReader reader) {
        value = reader.next2U4Double(); //u4
    }

    @Override
    public String getValue() {
        return value + "";
    }

    @Override
    public String toString() {
        return "Double:" + value;
    }

}
