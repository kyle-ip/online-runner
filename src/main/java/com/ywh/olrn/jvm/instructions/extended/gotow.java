package com.ywh.olrn.jvm.instructions.extended;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.instructions.base.BytecodeReader;
import com.ywh.olrn.jvm.rtda.Frame;

public class gotow extends BranchInstruction {

    @Override
    public int getOpCode() {
        return 0xc8;
    }

    @Override
    public void fetchOperands(BytecodeReader reader) throws Exception {
        offset = reader.read32();
    }

    @Override
    public void execute(Frame frame) throws Exception {
        branch(frame, offset);
    }
}
