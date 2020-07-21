package com.ywh.olrn.jvm.classfile.constantPool;

import com.ywh.olrn.jvm.classfile.ClassReader;
import lombok.Getter;


/**
 * 类/接口符号引用
 *
 * {
 *     u1 tag;
 *     u2 name_index;
 * }
 *
 * @author ywh
 * @since 21/07/2020
 */
@Getter
public class ConstantClassInfo implements ConstantInfo {

    private ConstantPool constPool;

    private int nameIndex;

    public ConstantClassInfo(ConstantPool aConstPool, ClassReader reader) {
        this.nameIndex = reader.nextU2ToInt();
        this.constPool = aConstPool;
    }

    @Override
    public String getValue() {
        return this.constPool.getUTF8(this.nameIndex);
    }


    @Override
    public String toString() {
        return constPool.getUTF8(nameIndex);
    }

}
