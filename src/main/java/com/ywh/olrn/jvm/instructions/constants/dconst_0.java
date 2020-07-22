package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * double 型 0 入操作数栈
 *
 * @author ywh
 * @since 11/07/2020
 */
public class dconst_0 extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x0e;
    }

    @Override
    public String getReName() {
        return "dconst_0";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushDouble(0.0d);
    }
}
