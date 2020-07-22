package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class ldc_w extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x13;
    }

    @Override
    public String getReName() {
        return "ldc_w";
    }

    @Override
    public void execute(Frame frame) {
    }
}
