package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 加载 int 类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class iload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x15;
    }

    @Override
    public String getReName() {
        return "iload";
    }

    @Override
    public void execute(Frame frame) throws Exception {
        int val = frame.getLocalVars().getInt(index);
        frame.getOperandStack().pushInt(val);
    }
}
