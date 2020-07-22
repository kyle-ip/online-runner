package com.ywh.olrn.jvm.instructions.comparisons;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;

public class if_icmple extends BranchInstruction {
    @Override
    public int getOpCode() {
        return 0xa4;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        int v2 = operandStack.popInt();
        int v1 = operandStack.popInt();
        if (v2 >= v1) {
            branch(frame,offset);
        }
    }
}
