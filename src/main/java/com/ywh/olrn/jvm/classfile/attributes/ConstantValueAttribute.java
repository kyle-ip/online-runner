package com.ywh.olrn.jvm.classfile.attributes;

import com.ywh.olrn.jvm.classfile.ClassReader;


/**
 * field 属性：常量表达式的值，存常量池索引
 *
 * ConstantValue_attribute {
 *     u2 attribute_name_index;
 *     u4 attribute_length;//2
 *     u2 constantvalue_index;//常量池索引
 * }
 *
 * @author ywh
 * @since 21/07/2020
 */
public class ConstantValueAttribute implements AttributeInfo {

    //具体指向哪种常量因字段类型而异。
    private int constantValueIndex;

    @Override
    public void readInfo(ClassReader reader) {
        constantValueIndex = reader.nextU2ToInt();
    }

}
