package com.ywh.olrn.jvm.classfile.constantPool;

import com.ywh.olrn.jvm.classfile.ClassReader;
import lombok.Getter;

/**
 * 字段/方法名和描述符
 *
 * CONSTANT_NameAndType_info {
 *     u1 tag;
 *     u2 name_index;//字段或方法名
 *     u2 descriptor_index;    //描述符
 * }
 *
 * @author ywh
 * @since 21/07/2020
 */
@Getter
public class ConstantNameAndTypeInfo implements ConstantInfo {

    private ConstantPool constPool;

    private int nameIndex;

    private int descriptorIndex;

    public ConstantNameAndTypeInfo(ConstantPool aConstPool, ClassReader reader) {
        this.nameIndex = reader.nextU2ToInt();
        this.descriptorIndex = reader.nextU2ToInt();
        this.constPool = aConstPool;
    }

    @Override
    public String getValue() {
        return "";
    }

    @Override
    public String toString() {
        return this.constPool.getUTF8(this.nameIndex) + "&"
                + this.constPool.getUTF8(this.descriptorIndex);
    }

}
