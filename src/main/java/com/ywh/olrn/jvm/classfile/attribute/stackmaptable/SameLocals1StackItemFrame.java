package com.ywh.olrn.jvm.classfile.attribute.stackmaptable;

import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo.VerificationTypeInfo;
import com.ywh.olrn.jvm.classfile.basestruct.U1;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class SameLocals1StackItemFrame extends StackMapFrame {
    public U1 sameLocals1StackItem = new U1();

    public U1 frameType = sameLocals1StackItem;

    public VerificationTypeInfo[] stack;
}
