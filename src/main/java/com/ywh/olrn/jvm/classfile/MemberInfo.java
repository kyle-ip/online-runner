package com.ywh.olrn.jvm.classfile;


import com.ywh.olrn.jvm.classfile.attributes.AttributeInfo;
import com.ywh.olrn.jvm.classfile.constantPool.ConstantPool;
import lombok.Getter;


/**
 * 成员信息：字段，方法
 *
 * field_info {
 * u2 access_flags;//访问标志
 * u2 name_index;//常量池索引 名字
 * u2 descriptor_index;//常量池索引  描述符
 * <p>
 * //属性表
 * u2 attributes_count;
 * attribute_info attributes[attributes_count];
 * }
 *
 * @author ywh
 * @since 21/07/2020
 */
@Getter
public class MemberInfo {

    private ConstantPool cp;

    private int accessFlags;

    private int nameIndex;

    private int descriptorIndex;

    private AttributeInfo[] attributes;

    public MemberInfo(ConstantPool constantPool, ClassReader reader) {
        cp = constantPool;
        accessFlags = reader.nextU2ToInt();
        nameIndex = reader.nextU2ToInt();
        descriptorIndex = reader.nextU2ToInt();
        attributes = AttributeInfo.readAttributes(reader, cp);
    }

    public static MemberInfo[] readMembers(ConstantPool constantPool, ClassReader reader) {
        int fieldCount = reader.nextU2ToInt();
        MemberInfo[] fields = new MemberInfo[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            fields[i] = new MemberInfo(constantPool, reader);
        }

        return fields;
    }

}
