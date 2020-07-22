package com.ywh.olrn.jvm.instructions.constants;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 什么也不做
 *
 * @author ywh
 * @since 11/07/2020
 */
public class nop extends NoOperandsInstruction {


    @Override
    public int getOpCode() {
        return 0x00;
    }

    @Override
    public String getReName() {
        return "nop";
    }

    @Override
    public void execute(Frame frame) {

    }
}
