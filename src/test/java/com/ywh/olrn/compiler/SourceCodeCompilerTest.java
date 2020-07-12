package com.ywh.olrn.compiler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("测试 Java 源码编译器")
class SourceCodeCompilerTest {


    @DisplayName("[true] 测试编译 Java 源码")
    @Test
    void compileTrue() throws Exception {
        String sourceCode = ""
            + "public class Run {\n"
            + "    public static void main(String[] args) {\n"
            + "        System.out.println(\"Hello, World!\");\n"
            + "    }\n"
            + "}";
        assertNotNull(SourceCodeCompiler.compile(sourceCode));
    }

    @DisplayName("[exception] 测试编译 Java 源码")
    @Test()
    void compileException() {
        String sourceCode = ""
            + "public class Run \n"
            + "    public static void main(String[] args) {\n"
            + "        System.out.println(\"Hello, World!\");\n"
            + "    }\n"
            + "}";
        try {
            SourceCodeCompiler.compile(sourceCode);
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
    }
}