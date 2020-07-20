package com.ywh.olrn.jvm.classfile.attribute.LocalVariableTypeTable;


import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class LocalVariableType {
    public U2 startPc;

    public U2 length;

    public U2 nameIndex;

    public U2 signatureIndex;

    public U2 index;
}
