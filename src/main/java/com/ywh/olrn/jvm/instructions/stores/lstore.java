package com.ywh.olrn.jvm.instructions.stores;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class lstore extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x37;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        long val = frame.getOperandStack().popLong();
        frame.getLocalVars().setLong(index, val);
    }
}
