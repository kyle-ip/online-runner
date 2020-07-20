package com.ywh.olrn.jvm.classfile.attribute.runtimeVisibleAnnotations;


import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class Annotation {
    public U2 typeIndex;

    public U2 numElementValuePairs;

    public ElementValuePair[] elementValuePairs;
}
