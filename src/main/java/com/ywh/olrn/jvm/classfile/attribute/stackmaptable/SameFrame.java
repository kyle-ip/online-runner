package com.ywh.olrn.jvm.classfile.attribute.stackmaptable;

import com.ywh.olrn.jvm.classfile.basestruct.U1;

/**
 *
 * @author ywh
 * @since 20/07/2020
 */
public class SameFrame extends StackMapFrame {
    public U1 same = new U1();

    public U1 frameType = same;
}
