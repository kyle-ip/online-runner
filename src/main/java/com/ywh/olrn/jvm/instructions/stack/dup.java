package com.ywh.olrn.jvm.instructions.stack;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;
import com.ywh.olrn.jvm.rtda.Slot;

public class dup extends NoOperandsInstruction {
    @Override
    public int getOpCode() {
        return 0x59;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        Slot slot = operandStack.popSlot();
        operandStack.pushSlot(slot);
        operandStack.pushSlot(slot);
    }
}
