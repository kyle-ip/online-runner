package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class ldc2_w extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x14;
    }

    @Override
    public String getReName() {
        return "ldc2_w";
    }

    @Override
    public void execute(Frame frame) {
    }
}
