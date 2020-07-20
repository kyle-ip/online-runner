package com.ywh.olrn.jvm.classfile.attribute;


import com.ywh.olrn.jvm.classfile.attribute.code.ExceptionTable;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;
import com.ywh.olrn.jvm.classfile.basestruct.U4;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class CodeAttribute extends AttributeBase {
    public U2 attributeNameIndex;

    public U4 attributeLength;

    public U2 maxStack;

    public U2 maxLocals;

    public U4 codeLength;

    public U1[] code;

    public U2 exceptionTableLength;

    public ExceptionTable[] exceptionTables;

    public U2 attributeCount;

    public AttributeBase[] attributes;
}
