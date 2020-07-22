package com.ywh.olrn.jvm.instructions.stores;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

import java.lang.ref.Reference;

public class astore extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x3a;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        Reference reference = frame.getOperandStack().popRef();
        frame.getLocalVars().setRef(index, reference);
    }
}
