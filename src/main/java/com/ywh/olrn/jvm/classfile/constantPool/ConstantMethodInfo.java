package com.ywh.olrn.jvm.classfile.constantPool;

import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantMethodInfo implements ConstantInfo {

    private ConstantPool constPool;

    private int classIndex;

    private int nameAndTypeIndex;

    public ConstantMethodInfo(ConstantPool aConstPool, ClassReader reader) {
        this.classIndex = reader.nextU2ToInt();
        this.nameAndTypeIndex = reader.nextU2ToInt();
    }

    @Override
    public String getValue() {
        return "";
    }

}
