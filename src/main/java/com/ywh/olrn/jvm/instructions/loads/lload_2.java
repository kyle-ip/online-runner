package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class lload_2 extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x20;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        long val = frame.getLocalVars().getLong(2);
        frame.getOperandStack().pushLong(val);
    }
}
