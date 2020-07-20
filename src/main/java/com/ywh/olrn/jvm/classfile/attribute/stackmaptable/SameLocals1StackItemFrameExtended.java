package com.ywh.olrn.jvm.classfile.attribute.stackmaptable;

import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo.VerificationTypeInfo;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class SameLocals1StackItemFrameExtended extends StackMapFrame {
    public U1 sameLocals1StackItemExtended = new U1();

    public U1 frameType = sameLocals1StackItemExtended;

    public U2 offsetDelta;

    public VerificationTypeInfo[] stack;
}
