package com.ywh.olrn.executor;

import java.io.*;

/**
 * 自定义输出流，为每个线程保持一个标准输出流
 * @author ywh
 * @since 12/07/2020
 */
public class CustomPrintStream extends PrintStream {

    private final ThreadLocal<ByteArrayOutputStream> out;

    private final ThreadLocal<Boolean> trouble;

    public CustomPrintStream() {
        super(new ByteArrayOutputStream());
        out = new ThreadLocal<>();
        trouble = new ThreadLocal<>();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return out.get().toString();
    }

    /**
     *
     */
    private void ensureOpen() {
        if (out.get() == null) {
            out.set(new ByteArrayOutputStream());
        }
    }

    /**
     *
     */
    @Override
    public void flush() {
        try {
            ensureOpen();
            out.get().flush();
        }
        catch (IOException x) {
            trouble.set(true);
        }
    }

    /**
     *
     */
    @Override
    public void close() {
        try {
            out.get().close();
        }
        catch (IOException x) {
            trouble.set(true);
        }
        out.remove();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean checkError() {
        if (out.get() != null) {
            flush();
        }
        return trouble.get() != null ? trouble.get() : false;
    }

    /**
     *
     */
    @Override
    protected void setError() {
        trouble.set(true);
    }

    /**
     *
     */
    @Override
    protected void clearError() {
        trouble.remove();
    }

    /**
     *
     * @param b
     */
    @Override
    public void write(int b) {
        try {
            ensureOpen();
            out.get().write(b);
            if ((b == '\n')) {
                out.get().flush();
            }
        }
        catch (InterruptedIOException x) {
            Thread.currentThread().interrupt();
        }
        catch (IOException x) {
            trouble.set(true);
        }
    }

    /**
     *
     * @param buf
     * @param off
     * @param len
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        ensureOpen();
        out.get().write(buf, off, len);
    }

    /**
     *
     * @param buf
     */
    private void write(char[] buf) {
        try {
            ensureOpen();
            out.get().write(new String(buf).getBytes());
        }
        catch (InterruptedIOException x) {
            Thread.currentThread().interrupt();
        }
        catch (IOException x) {
            trouble.set(true);
        }
    }

    /**
     *
     * @param s
     */
    private void write(String s) {
        try {
            ensureOpen();
            out.get().write(s.getBytes());
        }
        catch (InterruptedIOException x) {
            Thread.currentThread().interrupt();
        }
        catch (IOException x) {
            trouble.set(true);
        }
    }

    /**
     *
     */
    private void newLine() {
        try {
            ensureOpen();
            out.get().write(System.lineSeparator().getBytes());
        }
        catch (InterruptedIOException x) {
            Thread.currentThread().interrupt();
        }
        catch (IOException x) {
            trouble.set(true);
        }
    }

    /**
     *
     * @param b
     */
    @Override
    public void print(boolean b) {
        write(b ? "true" : "false");
    }

    /**
     *
     * @param c
     */
    @Override
    public void print(char c) {
        write(String.valueOf(c));
    }

    /**
     *
     * @param i
     */
    @Override
    public void print(int i) {
        write(String.valueOf(i));
    }

    /**
     *
     * @param l
     */
    @Override
    public void print(long l) {
        write(String.valueOf(l));
    }

    /**
     *
     * @param f
     */
    @Override
    public void print(float f) {
        write(String.valueOf(f));
    }

    /**
     *
     * @param d
     */
    @Override
    public void print(double d) {
        write(String.valueOf(d));
    }

    /**
     *
     * @param s
     */
    @Override
    public void print(char[] s) {
        write(s);
    }

    /**
     *
     * @param s
     */
    @Override
    public void print(String s) {
        if (s == null) {
            s = "null";
        }
        write(s);
    }

    /**
     *
     * @param obj
     */
    @Override
    public void print(Object obj) {
        write(String.valueOf(obj));
    }

    /**
     *
     */
    @Override
    public void println() {
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(boolean x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(char x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(int x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(long x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(float x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(double x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(char[] x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(String x) {
        print(x);
        newLine();
    }

    /**
     *
     * @param x
     */
    @Override
    public void println(Object x) {
        String s = String.valueOf(x);
        print(s);
        newLine();
    }
}
