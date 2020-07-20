package com.ywh.olrn.jvm.classfile.attribute;

import com.ywh.olrn.jvm.classfile.attribute.RuntimeVisibleParameterAnnotations.ParameterAnnotation;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;
import com.ywh.olrn.jvm.classfile.basestruct.U4;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class RuntimeInvisibleParameterAnnotationsAttribute extends AttributeBase {
    public U2 attributeNameIndex;

    public U4 attributeLength;

    public U1 numParameters;

    public ParameterAnnotation[] parameterAnnotations;
}
