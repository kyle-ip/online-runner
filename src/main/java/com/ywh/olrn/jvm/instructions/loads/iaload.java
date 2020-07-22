package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class iaload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x2e;
    }

    @Override
    public void execute(Frame frame) {

    }
}
