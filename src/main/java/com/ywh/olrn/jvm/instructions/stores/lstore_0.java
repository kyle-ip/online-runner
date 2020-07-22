package com.ywh.olrn.jvm.instructions.stores;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class lstore_0 extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x3f;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        long val = frame.getOperandStack().popLong();
        frame.getLocalVars().setLong(0, val);
    }
}
