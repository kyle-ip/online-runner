package com.ywh.olrn.jvm.instructions.conversions;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;

public class f2i extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x8b;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        operandStack.pushInt((int) operandStack.popFloat());
    }
}
