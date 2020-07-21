package com.ywh.olrn.jvm.classfile.constantPool;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 整型常量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantIntegerInfo implements ConstantInfo {

    private Integer value;

    /**
     * 4bytes
     *
     * @param reader
     */
    public ConstantIntegerInfo(ClassReader reader) {
        value = reader.nextU4ToInt();
    }

    @Override
    public String getValue() {
        return value + "";
    }

    @Override
    public String toString() {
        return "Integer: " + value;
    }

}
