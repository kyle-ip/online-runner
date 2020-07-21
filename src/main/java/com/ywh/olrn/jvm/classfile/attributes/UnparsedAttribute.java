package com.ywh.olrn.jvm.classfile.attributes;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 *
 * @author ywh
 * @since 21/07/2020
 */
public class UnparsedAttribute implements AttributeInfo {

    private String name;

    private int length;

    private byte[] info;

    public UnparsedAttribute(String name, int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public void readInfo(ClassReader reader) {
        info = reader.nextBytes(length);
    }

}
