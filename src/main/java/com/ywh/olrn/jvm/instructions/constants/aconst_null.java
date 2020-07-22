package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * null 引用入操作数栈
 *
 * @author ywh
 * @since 11/07/2020
 */
public class aconst_null extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x01;
    }

    @Override
    public String getReName() {
        return "aconst_null";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushRef(null);
    }
}
