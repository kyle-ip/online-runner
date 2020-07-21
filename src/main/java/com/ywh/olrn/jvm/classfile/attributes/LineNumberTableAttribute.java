package com.ywh.olrn.jvm.classfile.attributes;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 方法行号
 *
 * @author ywh
 * @since 21/07/2020
 */
public class LineNumberTableAttribute implements AttributeInfo {

    private LineNumberTableEntry[] lineNumberTable;

    @Override
    public void readInfo(ClassReader reader) {
        int length = reader.nextU2ToInt();
        lineNumberTable = new LineNumberTableEntry[length];
        for (int i = 0; i < length; i++) {
            lineNumberTable[i] = new LineNumberTableEntry(reader);
        }
    }

    private static class LineNumberTableEntry {
        private final int startPc;

        private final int lineNumber;

        public LineNumberTableEntry(ClassReader reader) {
            startPc = reader.nextU2ToInt();
            lineNumber = reader.nextU2ToInt();
        }
    }

}
