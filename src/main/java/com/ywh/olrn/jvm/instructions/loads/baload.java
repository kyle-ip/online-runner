package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class baload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x33;
    }

    @Override
    public void execute(Frame frame) {

    }
}
