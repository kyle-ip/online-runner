package com.ywh.olrn.jvm.classpath.entry;

import com.ywh.olrn.jvm.classpath.BaseEntry;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;

/**
 * 访问类路径（从压缩包）
 *
 * @author ywh
 * @since 2020/7/17 15:36
 */
public class ZipEntry extends BaseEntry {

    public ZipEntry(String path) {
        super(path);
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        assert className != null && className.length() > 0;
        FileInputStream input = new FileInputStream(this.path);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), StandardCharsets.UTF_8);
        java.util.zip.ZipEntry ze;
        while ((ze = zipInputStream.getNextEntry()) != null) {
            if (!className.equals(ze.getName())) {
                continue;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while (-1 != (n = zipInputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
        return null;
    }

}