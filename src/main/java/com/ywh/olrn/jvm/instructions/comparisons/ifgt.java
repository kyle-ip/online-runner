package com.ywh.olrn.jvm.instructions.comparisons;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;

public class ifgt extends BranchInstruction {
    @Override
    public int getOpCode() {
        return 0x9d;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        int i = operandStack.popInt();
        if (i < 0) {
            branch(frame,offset);
        }
    }
}
