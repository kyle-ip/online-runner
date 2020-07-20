package com.ywh.olrn.jvm.classfile;


import com.ywh.olrn.jvm.classfile.attribute.AttributeBase;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class MethodInfo {

    public U2 accessFlags;

    public U2 nameIndex;

    public U2 descriptorIndex;

    public U2 attributeCount;

    public AttributeBase[] attributes;

    public int argSlotCount = -1;
//    public JavaClass javaClass;
}
