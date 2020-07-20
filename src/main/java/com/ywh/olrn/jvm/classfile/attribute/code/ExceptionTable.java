package com.ywh.olrn.jvm.classfile.attribute.code;

import com.ywh.olrn.jvm.classfile.basestruct.U2;

/**
 * @author ywh
 * @since 20/07/2020
 */
public class ExceptionTable {
    public U2 startPc;

    public U2 endPc;

    public U2 handlerPc;

    public U2 catchType;
}
