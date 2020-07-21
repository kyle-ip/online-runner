package com.ywh.olrn.jvm.classfile.attributes;

import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 方法局部变量
 *
 * @author ywh
 * @since 21/07/2020
 */
public class LocalVariableTableAttribute implements AttributeInfo {
    private LocalVariableTableEntry[] localVariableTable;

    @Override
    public void readInfo(ClassReader reader) {
        int length = reader.nextU2ToInt();
        localVariableTable = new LocalVariableTableEntry[length];
        for (int i = 0; i < length; i++) {
            localVariableTable[i] = new LocalVariableTableEntry(reader);
        }
    }

    private static class LocalVariableTableEntry {
        private final int startPc;
        private final int length;
        private final int nameIndex;
        private final int descriptorIndex;
        private final int index;

        public LocalVariableTableEntry(ClassReader reader) {
            startPc = reader.nextU2ToInt();
            length = reader.nextU2ToInt();
            nameIndex = reader.nextU2ToInt();
            descriptorIndex = reader.nextU2ToInt();
            index = reader.nextU2ToInt();
        }
    }

}
