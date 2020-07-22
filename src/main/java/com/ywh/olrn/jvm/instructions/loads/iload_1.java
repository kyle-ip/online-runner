package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.NoOperandsInstruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 *
 * @author ywh
 * @since 11/07/2020
 */
public class iload_1 extends NoOperandsInstruction {

    @Override
    public int getOpCode() {
        return 0x1b;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        int val = frame.getLocalVars().getInt(1);
        frame.getOperandStack().pushInt(val);
    }
}
