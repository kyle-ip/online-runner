package com.ywh.olrn.executor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 自定义输入流，为每个线程保持一个标准输入流
 * @author ywh
 * @since 12/07/2020
 */
public class CustomInputStream extends InputStream {

    public final static ThreadLocal<InputStream> HOLD_INPUT_STREAM = new ThreadLocal<>();

    @Override
    public int read() {
        return 0;
    }

    public InputStream get() {
        return HOLD_INPUT_STREAM.get();
    }

    public void set(String systemIn) {
        HOLD_INPUT_STREAM.set(new ByteArrayInputStream(systemIn.getBytes()));
    }

    @Override
    public void close() {
        HOLD_INPUT_STREAM.remove();
    }
}
