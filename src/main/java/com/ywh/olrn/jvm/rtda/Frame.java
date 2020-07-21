package com.ywh.olrn.jvm.rtda;

import com.ywh.olrn.jvm.classfile.attributes.CodeAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 栈帧
 * 局部变量表大小、操作数栈深度在 {@link CodeAttribute} 定义
 *
 * @author ywh
 * @since 21/07/2020
 */
@Setter
@Getter
@ToString
public class Frame {

    private Frame lower;

    /**
     * 局部变量表
     */
    private LocalVars localVars;

    /**
     * 操作数栈
     */
    private OperandStack operandStack;

    /**
     * 线程本身
     */
    private Thread thread;

    private int nextPc;

    public Frame(Thread thread, int maxLocals, int maxStack) {
        this.thread = thread;
        this.localVars = new LocalVars(maxLocals);
        this.operandStack = new OperandStack(maxStack);
    }

}
