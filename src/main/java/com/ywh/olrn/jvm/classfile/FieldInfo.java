package com.ywh.olrn.jvm.classfile;


import com.ywh.olrn.jvm.classfile.attribute.AttributeBase;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class FieldInfo {

    public U2 accessFlags;

    public U2 nameIndex;

    public U2 descriptorIndex;

    public U2 attributeCount;

    public AttributeBase[] attributes;

    public int slotId;

    public int constValueIndex;

//    public JavaClass javaClass;

}
