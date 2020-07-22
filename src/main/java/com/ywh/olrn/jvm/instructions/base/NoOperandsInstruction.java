package com.ywh.olrn.jvm.instructions.base;

import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 无操作数指令
 *
 * @author ywh
 * @since 22/07/2020
 */
public abstract class NoOperandsInstruction implements Instruction {

    /**
     * 取操作数
     *
     * @param reader
     */
    @Override
    public void fetchOperands(BytecodeReader reader) {

    }

    @Override
    public abstract void execute(Frame frame) throws Exception;
}
