package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 加载 double 类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class dload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x18;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        double val = frame.getLocalVars().getDouble(index);
        frame.getOperandStack().pushDouble(val);
    }
}
