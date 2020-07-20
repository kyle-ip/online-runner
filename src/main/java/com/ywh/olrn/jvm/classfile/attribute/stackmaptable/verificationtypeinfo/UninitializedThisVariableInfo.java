package com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo;

import com.ywh.olrn.jvm.classfile.basestruct.U1;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class UninitializedThisVariableInfo extends VerificationTypeInfo {
    public U1 itemUninitializedthis = new U1();

    public U1 tag = itemUninitializedthis;
}
