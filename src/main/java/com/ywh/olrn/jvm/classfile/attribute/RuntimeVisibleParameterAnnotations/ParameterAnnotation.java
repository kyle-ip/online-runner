package com.ywh.olrn.jvm.classfile.attribute.RuntimeVisibleParameterAnnotations;

import com.ywh.olrn.jvm.classfile.attribute.runtimeVisibleAnnotations.Annotation;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class ParameterAnnotation {
    public U2 numAnnotations;

    public Annotation[] annotations;
}
