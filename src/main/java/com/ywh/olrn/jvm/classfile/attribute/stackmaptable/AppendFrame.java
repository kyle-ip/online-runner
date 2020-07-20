package com.ywh.olrn.jvm.classfile.attribute.stackmaptable;

import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo.VerificationTypeInfo;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class AppendFrame extends StackMapFrame {
    public U1 append = new U1();

    public U1 frameType = append;

    public U2 offsetDelta;

    public VerificationTypeInfo[] locals ;
}
