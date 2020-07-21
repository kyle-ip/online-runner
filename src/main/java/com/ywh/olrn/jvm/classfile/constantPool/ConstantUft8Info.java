package com.ywh.olrn.jvm.classfile.constantPool;

import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * Modified UTF8 字符串型常量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantUft8Info implements ConstantInfo {

    private String value;

    public ConstantUft8Info(ClassReader reader) {
        int length = reader.nextU2ToInt();
        byte[] bytes = reader.nextBytes(length);
        this.value = new String(bytes);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

}
