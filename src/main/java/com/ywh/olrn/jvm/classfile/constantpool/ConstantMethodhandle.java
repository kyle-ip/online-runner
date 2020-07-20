package com.ywh.olrn.jvm.classfile.constantpool;

import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class ConstantMethodhandle extends ConstantInfo {
    public U1 tag;

    public U1 referenceKind;

    public U2 referenceIndex;
}
