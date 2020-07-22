package com.ywh.olrn.jvm.instructions.control;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class lreturn extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0xad;
    }

    @Override
    public void execute(Frame frame) throws Exception {

    }
}
