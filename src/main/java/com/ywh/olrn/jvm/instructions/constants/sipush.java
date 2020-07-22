package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.BytecodeReader;
import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

public class sipush extends NoOperandsInstruction {

    private int val;

    @Override
    public int getOpCode() {
        return 0x11;
    }

    @Override
    public String getReName() {
        return "sipush";
    }

    @Override
    public void fetchOperands(BytecodeReader reader) {
        val = reader.read16();
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushInt(val);
    }
}
