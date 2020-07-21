package com.ywh.olrn.jvm.classfile.constantPool;


import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 字符串符号引用
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantStringInfo implements ConstantInfo {

    private ConstantPool constPool;

    private int nameIndex;

    public ConstantStringInfo(ConstantPool aConstPool, ClassReader reader) {
        this.nameIndex = reader.nextU2ToInt();
        this.constPool = aConstPool;
    }

    @Override
    public String getValue() {
        return this.constPool.getUTF8(this.nameIndex);
    }

    @Override
    public String toString() {
        return "ConstStringInfo{" +
                "name=" + constPool.getUTF8(nameIndex) +
                '}';
    }

}
