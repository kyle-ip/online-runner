package com.ywh.olrn.jvm.classfile.attribute.runtimeVisibleAnnotations;

import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class ArrayValue extends ElementValue {
    public U1 tag;
    public U2 numValues;
    public ElementValue[] values;
}
