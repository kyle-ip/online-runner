package com.ywh.olrn.jvm.instructions.base;

import com.ywh.olrn.jvm.rtda.Frame;
import lombok.Setter;

/**
 * 存储和加载指令
 * 需要根据索引存取局部变量表，索引由单字节操作数给出
 *
 * @author ywh
 * @since 22/07/2020
 */
public abstract class Index8Instruction implements Instruction {

    /**
     * 索引
     */
    @Setter
    protected int index;

    @Override
    public void fetchOperands(BytecodeReader reader) {
        this.index = reader.read8();
    }

    @Override
    public abstract void execute(Frame frame) throws Exception;
}
