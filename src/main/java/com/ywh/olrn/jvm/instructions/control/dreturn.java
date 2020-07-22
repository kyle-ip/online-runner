package com.ywh.olrn.jvm.instructions.control;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class dreturn extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0xaf;
    }

    @Override
    public void execute(Frame frame) throws Exception {

    }
}
