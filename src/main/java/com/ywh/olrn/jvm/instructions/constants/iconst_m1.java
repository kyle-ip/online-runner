package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * int 型 -1 入操作数栈
 *
 * @author ywh
 * @since 11/07/2020
 */
public class iconst_m1 extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x02;
    }

    @Override
    public String getReName() {
        return "iconst_m1";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        frame.getOperandStack().pushInt(-1);
    }
}
