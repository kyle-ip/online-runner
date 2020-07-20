package com.ywh.olrn.jvm.classfile.attribute.stackmaptable;

import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo.VerificationTypeInfo;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class FullFrame extends StackMapFrame {
    public U1 fullFrame = new U1();
    public U1 frameType = fullFrame;
    public U2 offsetDelta;
    public U2 numberOfLocals;
    public VerificationTypeInfo[] locals;
    public U2 numberOfStackItems;
    public VerificationTypeInfo[] stack;
}
