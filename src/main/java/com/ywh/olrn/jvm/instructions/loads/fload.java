package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 加载 float 类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class fload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x17;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        float val = frame.getLocalVars().getFloat(index);
        frame.getOperandStack().pushFloat(val);
    }
}
