package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

import java.lang.ref.Reference;

/**
 * 加载引用类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class aload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0x19;
    }

    @Override
    public void execute(Frame frame) throws Exception {
        Reference ref = frame.getLocalVars().getRef(index);
        frame.getOperandStack().pushRef(ref);
    }
}
