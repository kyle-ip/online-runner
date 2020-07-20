package com.ywh.olrn.jvm.classfile.attribute.LocalVariableTable;


import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class LocalVariable {
    public U2 startPc;

    public U2 length;

    public U2 nameIndex;

    public U2 descriptorIndex;

    public U2 index;
}
