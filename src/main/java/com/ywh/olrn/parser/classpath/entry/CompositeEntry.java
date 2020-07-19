package com.ywh.olrn.parser.classpath.entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ywh.olrn.constant.FileConstants.PATH_LIST_SEPARATOR;

/**
 * 访问类路径列表
 *
 * @author ywh
 * @since 2020/7/17 15:36
 */
public class CompositeEntry extends Entry {

    private final List<Entry> entries = new ArrayList<>();

    public CompositeEntry(String path) throws IOException {
        super();
        for (String s : path.split(PATH_LIST_SEPARATOR)) {
            this.entries.add(newEntry(s));
        }
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