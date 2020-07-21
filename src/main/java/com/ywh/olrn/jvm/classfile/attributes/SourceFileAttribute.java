package com.ywh.olrn.jvm.classfile.attributes;

import com.ywh.olrn.jvm.classfile.ClassReader;
import com.ywh.olrn.jvm.classfile.constantPool.ConstantPool;


/**
 * class 属性：源文件名，如 XXX.java
 *
 * ```
 * SourceFile_attribute {
 * u2 attribute_name_index;
 * u4 attribute_length;//必须2
 * u2 sourcefile_index;//常量池索引，指向CONSTANT_Utf8_info常量
 * }
 * ```
 *
 * @author ywh
 * @since 21/07/2020
 */
public class SourceFileAttribute implements AttributeInfo {

    private ConstantPool cp;

    private String sourceFile;

    public SourceFileAttribute(ConstantPool cp) {
        this.cp = cp;
    }

    @Override
    public void readInfo(ClassReader reader) {
        int sourceFileIndex = reader.nextU2ToInt();
        sourceFile = cp.getUTF8(sourceFileIndex);
    }

}
