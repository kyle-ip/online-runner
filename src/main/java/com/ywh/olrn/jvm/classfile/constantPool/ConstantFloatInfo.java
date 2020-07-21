package com.ywh.olrn.jvm.classfile.constantPool;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 浮点型常量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantFloatInfo implements ConstantInfo {

    private float value;

    /**
     * 4bytes
     * @param reader
     */
    public ConstantFloatInfo(ClassReader reader) {
        value = reader.nextU4ToFloat();
    }

    @Override
    public String getValue() {
        return value + "";
    }

    @Override
    public String toString() {
        return "Float: " + value;
    }

}
