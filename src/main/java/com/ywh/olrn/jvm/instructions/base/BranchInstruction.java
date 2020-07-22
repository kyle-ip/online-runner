package com.ywh.olrn.jvm.instructions.base;

import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 跳转指令
 *
 * @author ywh
 * @since 22/07/2020
 */
public abstract class BranchInstruction implements Instruction {

    /**
     * 跳转偏移量
     */
    protected int offset;

    @Override
    public void fetchOperands(BytecodeReader reader) throws Exception {
        this.offset = reader.read16();
    }

    @Override
    public abstract void execute(Frame frame) throws Exception;

    public void branch(Frame frame, int offset) {
        int pc = frame.getThread().getPc();
        frame.setNextPc(pc + offset);
    }
}
