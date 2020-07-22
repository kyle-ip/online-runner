package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class fload_2 extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x24;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        float val = frame.getLocalVars().getFloat(2);
        frame.getOperandStack().pushFloat(val);
    }
}
