package com.ywh.olrn.jvm.instructions.control;

import com.ywh.olrn.jvm.instructions.base.BranchInstruction;
import com.ywh.olrn.jvm.instructions.base.BytecodeReader;
import com.ywh.olrn.jvm.rtda.Frame;

public class goto_ extends BranchInstruction {
    @Override
    public int getOpCode() {
        return 0xa7;
    }

    @Override
    public void fetchOperands(BytecodeReader reader) throws Exception {
        this.offset = reader.read16();
    }

    @Override
    public void execute(Frame frame) throws Exception {
        branch(frame, offset);
    }
}
