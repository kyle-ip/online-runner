package com.ywh.olrn.jvm.rtda;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 线程
 *
 * @author ywh
 * @since 21/07/2020
 */
@Setter
@Getter
@ToString
public class Thread {

    /**
     * 栈指针
     */
    private int pc;

    /**
     * Java 虚拟机栈
     */
    private Stack stack;

    public Thread(int maxStack) {
        this.stack = new Stack(maxStack);
    }

    public void pushFrame(Frame frame) {
        this.stack.push(frame);
    }

    public Frame popFrame() {
        return this.stack.pop();
    }

    public Frame currentFrame() {
        return this.stack.current();
    }
}
