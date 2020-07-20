package com.ywh.olrn.jvm.classfile.constantpool;

import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U4;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class ConstantLong extends ConstantInfo {
    public U1 tag;

    public U4 highBytes;

    public U4 lowBytes;
}
