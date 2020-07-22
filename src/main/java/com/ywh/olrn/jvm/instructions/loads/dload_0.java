package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class dload_0 extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x26;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        double val = frame.getLocalVars().getDouble(0);
        frame.getOperandStack().pushDouble(val);
    }
}
