package com.ywh.olrn.jvm.classpath;

import com.ywh.olrn.jvm.classpath.entry.CompositeEntry;
import com.ywh.olrn.jvm.classpath.entry.DirEntry;
import com.ywh.olrn.jvm.classpath.entry.WildcardEntry;
import com.ywh.olrn.jvm.classpath.entry.ZipEntry;

import java.io.File;
import java.io.IOException;

import static com.ywh.olrn.constant.FileConstants.*;

/**
 * @author ywh
 * @since 2020/7/17 15:34
 */
public abstract class BaseEntry {

    protected String path;

    public BaseEntry() {
    }

    public BaseEntry(String path) {
        this.path = path;
    }

    public static BaseEntry newEntry(String path) throws IOException {
        File pathObj = new File(path);
        if (!pathObj.exists()) {
            throw new RuntimeException("Can't find entry dir!");
        }
        path = pathObj.getAbsolutePath();
        if (path.contains(PATH_SEPARATOR)) {
            return new CompositeEntry(path);
        }
        if (path.endsWith(VAGUE)) {
            return new WildcardEntry(path);
        }
        if (path.endsWith(SUFFIX_JAR) || path.endsWith(SUFFIX_JAR.toUpperCase())
            || path.endsWith(SUFFIX_ZIP) || path.endsWith(SUFFIX_ZIP.toUpperCase())
        ) {
            return new ZipEntry(path);
        }
        return new DirEntry(path);
    }

    public byte[] readClass(String className) throws IOException {
        throw null;
    }

    @Override
    public String toString() {
        return path;
    }
}