package com.ywh.olrn.jvm.classfile.attribute;


import com.ywh.olrn.jvm.classfile.attribute.runtimeVisibleAnnotations.Annotation;
import com.ywh.olrn.jvm.classfile.basestruct.U2;
import com.ywh.olrn.jvm.classfile.basestruct.U4;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class RuntimeVisibleAnnotationsAttribute extends AttributeBase {
    public U2 attributeNameIndex;
    public U4 attributeLength;
    public U2 numAnnotations;
    public Annotation[] annotations;
}
