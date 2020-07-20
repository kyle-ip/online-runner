package com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo;


import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class UninitializedVariableInfo extends VerificationTypeInfo {
    public U1 itemUninitialized = new U1();

    public U1 tag = itemUninitialized;

    public U2 offset;
}
