package com.ywh.olrn.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ywh
 * @since 11/07/2020
 */
public class FileUtil {

    /**
     * 写入文件
     *
     * @param data
     * @param fileName
     */
    public static void write(String data, String fileName) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); PrintWriter out = new PrintWriter(fw)) {
            out.write(data);
            out.println();
        }
    }
}
