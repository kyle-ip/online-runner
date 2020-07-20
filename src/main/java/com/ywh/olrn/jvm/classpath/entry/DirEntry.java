package com.ywh.olrn.jvm.classpath.entry;

import com.ywh.olrn.jvm.classpath.BaseEntry;
import java.io.IOException;
import static com.ywh.olrn.constant.FileConstants.PATH_SEPARATOR;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

/**
 * 访问类路径
 *
 * @author ywh
 * @since 2020/7/17 15:36
 */
public class DirEntry extends BaseEntry {

    public DirEntry(String path) {
        super(path);
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        assert className != null && className.length() > 0;
        String fileName = String.join(PATH_SEPARATOR, this.path, className);
        return readAllBytes(get(fileName));
    }

}