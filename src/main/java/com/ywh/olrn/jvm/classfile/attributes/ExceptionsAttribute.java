package com.ywh.olrn.jvm.classfile.attributes;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * method 属性：变长属性，记录方法抛出的异常表
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ExceptionsAttribute implements AttributeInfo {

    private int[] exceptionIndexTable;

    @Override
    public void readInfo(ClassReader reader) {
        exceptionIndexTable = reader.nextUint16s();
    }

}
