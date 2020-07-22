package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class lconst_0 extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x09;
    }

    @Override
    public String getReName() {
        return "lconst_0";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushLong(0L);
    }
}
