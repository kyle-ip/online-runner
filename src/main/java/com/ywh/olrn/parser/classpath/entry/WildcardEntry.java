package com.ywh.olrn.parser.classpath.entry;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static com.ywh.olrn.constant.FileConstants.VAGUE;
import static com.ywh.olrn.constant.FileConstants.PATH_SEPARATOR;

/**
 * 访问类路径（模糊匹配）
 *
 * @author ywh
 * @since 2020/7/17 15:36
 */
public class WildcardEntry extends Entry {

    private final List<Entry> entries = new ArrayList<>();

    public WildcardEntry(String path) throws IOException {
        super();
        path = path.split(VAGUE)[0];
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String p = String.join(PATH_SEPARATOR, dir.getParent().toString(), dir.getFileName().toString());
                entries.add(newEntry(p));
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        for (Entry entry : this.entries) {
            byte[] classByte =  entry.readClass(className);
            if (classByte != null) {
                return classByte;
            }
        }
        return null;
    }
}