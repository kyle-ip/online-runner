package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.BytecodeReader;
import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 从操作数栈中取一个 byte 型整数，扩展成 int 型再入栈
 *
 * @author ywh
 * @since 11/07/2020
 */
public class bipush extends NoOperandsInstruction {

    private int val;

    @Override
    public int getOpCode() {
        return 0x10;
    }

    @Override
    public String getReName() {
        return "bipush";
    }

    @Override
    public void fetchOperands(BytecodeReader reader) {
        val = reader.read8();
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushInt(val);
    }
}
