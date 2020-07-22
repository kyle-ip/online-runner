package com.ywh.olrn.jvm.instructions.stack;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class pop extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x57;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().popSlot();
    }
}
