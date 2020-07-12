package com.ywh.olrn.controller;

import com.ywh.olrn.compiler.SourceCodeCompiler;
import com.ywh.olrn.executor.ByteCodeExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author yipwinghong
 */
@RequestMapping("/")
@Controller
public class RunCodeController {

    private static final String TEMPLATE = "public class Run {\n"
        + "    public static void main(String[] args) {\n"
        + "        System.out.println(\"Hello, World!\");\n"
        + "    }\n"
        + "}";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("sourceCode", TEMPLATE);
        return "index";
    }

    @PostMapping("/")
    public String runCode(@RequestParam("sourceCode") String sourceCode,
                          @RequestParam("systemIn") String systemIn,
                          Model model) {
        model.addAttribute("sourceCode", sourceCode);
        model.addAttribute("systemIn", systemIn);
        byte[] byteCode;
        try {
            byteCode = SourceCodeCompiler.compile(sourceCode);
        } catch (Exception ex) {
            model.addAttribute("result", ex.getMessage().replaceAll(System.lineSeparator(), "<br/>"));
            return "index";
        }

        String result = ByteCodeExecutor.execute(byteCode, systemIn);
        result = result.replaceAll(System.lineSeparator(), "<br/>");
        model.addAttribute("result", result);
        return "index";
    }
}
