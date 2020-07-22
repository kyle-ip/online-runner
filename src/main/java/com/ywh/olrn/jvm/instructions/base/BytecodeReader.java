package com.ywh.olrn.jvm.instructions.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 字节码读取器
 *
 * @author ywh
 * @since 22/07/2020
 */
@Getter
@Setter
public class BytecodeReader {

    /**
     * 字节码
     */
    private byte[] code;

    /**
     * 程序计数器：定位读取的字节
     */
    private int pc;

    public BytecodeReader(byte[] code, int pc) {
        this.code = code;
        this.pc = pc;
    }

    /**
     * 重置
     *
     * @param code
     * @param pc
     * @return
     */
    public BytecodeReader reset(byte[] code, int pc) {
        this.code = code;
        this.pc = pc;
        return this;
    }

    /**
     * u1
     *
     * @return
     */
    public int read8() {
        int b = code[pc];
        pc += 1;
        return b & 0xFF;
    }

    /**
     * u2
     *
     * @return
     */
    public int read16() {
        int b1 = code[pc];
        int b2 = code[pc + 1];
        pc += 2;
        return b1 << 8 | b2;
    }

    /**
     * u4
     *
     * @return
     */
    public int read32() {
        int b1 = code[pc];
        int b2 = code[pc + 1];
        int b3 = code[pc + 2];
        int b4 = code[pc + 3];
        pc += 4;
        return b1 << 24 | b2 << 16 | b3 << 8 | b4;
    }

    /**
     *
     * @param length
     * @return
     */
    public int[] readInt32s(int length) {
        int[] ints = new int[length];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = read32();
        }
        return ints;
    }

    /**
     * 跳过对齐填充
     */
    public void skipPadding() {
        while (pc % 4 != 0) {
            read8();
        }
    }
}
