package com.ywh.olrn.executor;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * 自定义标准输入输出（确保线程安全）
 * 把要执行的类对 System 的符号引用替换为 CustomSystem 的符号引用
 * @author ywh
 * @since 12/07/2020
 */
public final class CustomSystem {

    private CustomSystem() {
    }

    public final static InputStream IN = new CustomInputStream();

    public final static PrintStream OUT = new CustomPrintStream();

    public final static PrintStream ERR = OUT;

    /**
     * 获取当前线程的输出流中的内容
     */
    public static String getBufferString() {
        return OUT.toString();
    }

    /**
     * 关闭当前线程的输入流和输出流
     */
    public static void closeBuffer() {
        ((CustomInputStream) IN).close();
        OUT.close();
    }
}
