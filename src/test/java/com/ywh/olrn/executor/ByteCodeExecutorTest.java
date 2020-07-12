package com.ywh.olrn.executor;

import com.ywh.olrn.compiler.SourceCodeCompiler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("测试 Java 字节码执行器")
class ByteCodeExecutorTest {

    private final String template = ""
        + "public class Run {\n"
        + "    public static void main(String[] args) {\n"
        + "        System.out.print(%s);\n"
        + "    }\n"
        + "}";

    @DisplayName("[true] 测试执行 Java 字节码：1 + 1")
    @Test
    void execute1() throws Exception{
        String sourceCode = String.format(template, "1 + 1");
        byte[] byteCode = SourceCodeCompiler.compile(sourceCode);
        assertEquals("2", ByteCodeExecutor.execute(byteCode, ""));
    }

    @DisplayName("[true] 测试执行 Java 字节码：Hello World")
    @Test
    void execute2() throws Exception {
        String sourceCode = String.format(template, "\"Hello, World!\"");
        byte[] byteCode = SourceCodeCompiler.compile(sourceCode);
        assertEquals("Hello, World!", ByteCodeExecutor.execute(byteCode, ""));
    }
}