package com.ywh.olrn.compiler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("测试 Java 源码编译器")
class SourceCodeCompilerTest {

    @DisplayName("[true] 测试编译 Java 源码")
    @Test
    void compileTrue() {
        String sourceCode = "public class Run {\n"
            + "    public static void main(String[] args) {\n"
            + "        System.out.println(\"Hello, World!\");\n"
            + "    }\n"
            + "}";
        assertNotNull(SourceCodeCompiler.compile(sourceCode));
    }
}