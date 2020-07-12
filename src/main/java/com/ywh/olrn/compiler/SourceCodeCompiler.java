package com.ywh.olrn.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 源码动态编译器
 *
 * @author ywh
 * @since 11/07/2020
 */
public class SourceCodeCompiler {

    /**
     * 预编译的正则模式，用于匹配类名
     */
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");


    /**
     * 编译源码
     *
     * javaCompiler.getTask(...).call() 执行流程：
     * 调用 {@link CustomJavaFileObject#getCharContent} 获取源码字符序列（源码字符串）
     * 编译器通过 {@link CustomJavaFileManager#getJavaFileForOutput} 获取 {@link CustomJavaFileObject} 对象
     * 编译器执行编译，通过 {@link CustomJavaFileObject#openOutputStream} 创建输出流对象容器，存放编译输出的字节码
     * 将字节码 {@link ByteArrayOutputStream} 转换为 byte[] 并返回
     *
     * @param sourceCode            源码字符串
     * @return
     */
    public static byte[] compile(String sourceCode) throws Exception {

        // 编译结果收集器（成功/失败信息）
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();

        // 获取 Java 编译器对象
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(diagnosticCollector, Locale.US, UTF_8);
        CustomJavaFileManager customJavaFileManager = new CustomJavaFileManager(standardJavaFileManager);

        // 获取类名
        Matcher matcher = CLASS_PATTERN.matcher(sourceCode);
        String className;
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class.");
        }

        // 执行编译
        boolean result = javaCompiler
            .getTask(null, customJavaFileManager, diagnosticCollector, null, null, Collections.singletonList(new CustomJavaFileObject(className, sourceCode)))
            .call();
        if (result) {
            return customJavaFileManager.getJavaFileForInput(className);
        }
//        StringBuilder compileErrorRes = new StringBuilder();

//        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
//            compileErrorRes.append(diagnostic.toString()).append(System.lineSeparator());
//        }
//        throw new Exception(compileErrorRes.toString());
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        throw new Exception(diagnostics.get(diagnostics.size() - 1).toString());

    }
}
