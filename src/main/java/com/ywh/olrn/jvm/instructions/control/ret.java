package com.ywh.olrn.jvm.instructions.control;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class ret extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0xa9;
    }

    @Override
    public void execute(Frame frame) throws Exception {

    }
}
