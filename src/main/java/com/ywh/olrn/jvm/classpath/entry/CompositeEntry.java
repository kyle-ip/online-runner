package com.ywh.olrn.jvm.classpath.entry;

import com.ywh.olrn.jvm.classpath.BaseEntry;

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
public class CompositeEntry extends BaseEntry {

    private final List<BaseEntry> entries = new ArrayList<>();

    public CompositeEntry(String path) throws IOException {
        super();
        for (String s : path.split(PATH_LIST_SEPARATOR)) {
            this.entries.add(newEntry(s));
        }
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        for (BaseEntry baseEntry : this.entries) {
            byte[] classByte =  baseEntry.readClass(className);
            if (classByte != null) {
                return classByte;
            }
        }
        return null;
    }

}