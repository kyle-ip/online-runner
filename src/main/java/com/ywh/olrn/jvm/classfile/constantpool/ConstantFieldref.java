package com.ywh.olrn.jvm.classfile.constantpool;

import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class ConstantFieldref extends ConstantInfo {
    public U1 tag;

    public U2 classIndex;

    public U2 nameAndTypeIndex;
}
