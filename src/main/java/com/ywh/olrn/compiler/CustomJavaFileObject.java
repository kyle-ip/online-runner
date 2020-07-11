package com.ywh.olrn.compiler;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 自定义源码、字节码封装类
 * 
 * @author ywh
 * @since 11/07/2020
 */
public class CustomJavaFileObject extends SimpleJavaFileObject {

    private final String sourceCode;

    private ByteArrayOutputStream byteArrayOutputStream;

    /**
     * 存储源码的 JavaFileObject
     */
    public CustomJavaFileObject(String name, String sourceCode) {
        super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    /**
     * 存储字节码的 JavaFileObject（JavaFileObject 的文件类型）
     */
    public CustomJavaFileObject(String name, JavaFileObject.Kind kind) {
        super(URI.create("String:///" + name + Kind.SOURCE.extension), kind);
        this.sourceCode = null;
    }

    /**
     * 获取源码字符序列
     *
     * @param ignoreEncodingErrors
     * @return
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        if (sourceCode == null) {
            throw new IllegalArgumentException("sourceCode == null");
        }
        return sourceCode;
    }

    /**
     * 创建输出流对象容器
     *
     * @return
     */
    @Override
    public OutputStream openOutputStream() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}