package com.ywh.olrn.parser.classpath;

import com.ywh.olrn.parser.classpath.entry.WildcardEntry;

import java.io.File;
import java.io.IOException;

import static com.ywh.olrn.constant.FileConstants.PATH_SEPARATOR;
import static com.ywh.olrn.constant.FileConstants.VAGUE;

/**
 * @author ywh
 * @since 2020/7/17 18:17
 */
public class Classpath {

    private Entry bootClasspath;

    private Entry extClasspath;

    private Entry userClasspath;

    public Classpath(String cpOption, String jreOption) {
        try {
            parseUserClasspath(cpOption);
            parseBootAndExtClasspath(jreOption);
        } catch (IOException ignored) {

        }
    }

    public byte[] readClass(String className) {
        byte[] classInfo = this.bootClasspath.readClass(className);
        if (classInfo != null) {
            return classInfo;
        }
        classInfo = this.extClasspath.readClass(className);
        if (classInfo != null) {
            return classInfo;
        }
        return this.userClasspath.readClass(className);
    }

    private void parseUserClasspath(String cpOption) throws IOException {
        if (cpOption == null || cpOption.isEmpty()) {
            cpOption = ".";
        }
        this.userClasspath = Entry.newEntry(cpOption);
    }

    private void parseBootAndExtClasspath(String jreOption) throws IOException {
        String jreDir = getJreDir(jreOption);
        String jreLibPath = String.join(PATH_SEPARATOR, jreDir, "lib", VAGUE);
        this.bootClasspath = new WildcardEntry(jreLibPath);
        String jreExtPath = String.join(PATH_SEPARATOR, jreDir, "lib", "ext", VAGUE);
        this.extClasspath = new WildcardEntry(jreExtPath);
    }

    private String getJreDir(String jreOption) {
        if (jreOption != null && jreOption.length() != 0) {
            return jreOption;
        }
        if (new File("./jre").exists()) {
            return new File("./jre").getAbsolutePath();
        }
        String javaHome = System.getenv().get("JAVA_HOME");
        if (javaHome != null) {
            return String.join(PATH_SEPARATOR, javaHome, "jre");
        }
        throw new RuntimeException("Can't find jre dir!");
    }
}