package com.ywh.olrn.jvm.instructions.comparisons;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.rtda.Frame;
import com.ywh.olrn.jvm.rtda.OperandStack;

import java.lang.ref.Reference;

public class if_acmpne extends BranchInstruction {
    @Override
    public int getOpCode() {
        return 0xa6;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        OperandStack operandStack = frame.getOperandStack();
        Reference v2 = operandStack.popRef();
        Reference v1 = operandStack.popRef();
        if (!v2.equals(v1)) {
            branch(frame,offset);
        }
    }
}
