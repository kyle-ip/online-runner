package com.ywh.olrn.jvm.instructions.loads;

import com.ywh.olrn.jvm.instructions.base.Index8Instruction;
import com.ywh.olrn.jvm.rtda.Frame;

/**
 * 加载数组类型
 *
 * @author ywh
 * @since 22/07/2020
 */
public class xaload extends Index8Instruction {
    @Override
    public int getOpCode() {
        return 0;
    }

    @Override
    public void execute(Frame frame) {

    }
}
