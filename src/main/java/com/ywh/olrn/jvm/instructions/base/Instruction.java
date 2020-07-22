package com.ywh.olrn.jvm.instructions.base;

import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 指令
 *
 * @author ywh
 * @since 22/07/2020
 */
public interface Instruction {

    int getOpCode();

    default String getReName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 从字节码中提取操作数
     *
     * @param reader
     * @throws Exception
     */
    void fetchOperands(BytecodeReader reader) throws Exception;

    /**
     * 执行逻辑指令
     *
     * @param frame
     * @throws Exception
     */
    void execute(Frame frame) throws Exception;
}
