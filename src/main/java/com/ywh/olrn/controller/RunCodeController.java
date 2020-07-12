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

    private static final String TEMPLATE = "" +
        "import java.util.Random;\n" +
        "\n" +
        "public class Main {\n" +
        "\n" +
        "    private final static int TEN_MILLION = 10_000_000;\n" +
        "\n" +
        "    public static void main(String[] args) {\n" +
        "        Random random = new Random();\n" +
        "        int[] arr = new int[TEN_MILLION ];\n" +
        "        for (int i = 0; i < TEN_MILLION ; ++i) {\n" +
        "            arr[i] = random.nextInt(TEN_MILLION );\n" +
        "        }\n" +
        "        for (int gap = arr.length >> 1; gap > 0; gap >>= 1) {\n" +
        "            for (int i = gap; i < arr.length; i++) {\n" +
        "                int j = i - gap, cur = arr[i];\n" +
        "                while (j >= 0 && arr[j] > cur) {\n" +
        "                    arr[j + gap] = arr[j];\n" +
        "                    j -= gap;\n" +
        "                }\n" +
        "                arr[j + gap] = cur;\n" +
        "            }\n" +
        "        }\n" +
        "    }\n" +
        "}";

//        "public class Run {\n"
//            +"    public static void main(String[] args) {\n"
//            +"        System.out.println(\"Hello, World!\");\n"
//            +"    }\n"
//            +"}";

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
        sourceCode = sourceCode.replaceAll("<", "&lt;");
        sourceCode = sourceCode.replaceAll(">", "&gt;");
        model.addAttribute("sourceCode", sourceCode);
        long start = System.currentTimeMillis();
        String result = ByteCodeExecutor.execute(byteCode, systemIn);
        long end = System.currentTimeMillis();
        result = result.replaceAll(System.lineSeparator(), "<br/>");
        model.addAttribute("result", result);
        model.addAttribute("runtime", end - start);
        return "index";
    }
}
