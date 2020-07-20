package com.ywh.olrn.jvm.classfile.attribute;


import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class BootstrapMethod {
    public U2 bootstrapMethodRef;

    public U2 numBootstrapArguments;

    public U2[] bootstrapArguments;
}
