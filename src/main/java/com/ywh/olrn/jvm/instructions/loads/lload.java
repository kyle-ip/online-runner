package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 加载 long 类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class lload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x16;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        long val = frame.getLocalVars().getLong(index);
        frame.getOperandStack().pushLong(val);
    }
}
