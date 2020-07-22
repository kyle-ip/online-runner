package com.ywh.olrn.jvm.instructions.conversions;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;

public class l2d extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x8a;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        operandStack.pushDouble(operandStack.popLong());
    }
}
