package com.ywh.olrn.jvm.instructions.stores;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class dstore_3 extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x4a;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        double val = frame.getOperandStack().popDouble();
        frame.getLocalVars().setDouble(3, val);
    }
}
