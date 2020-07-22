package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class iconst_1 extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x04;
    }

    @Override
    public String getReName() {
        return "iconst_1";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushInt(1);
    }
}
