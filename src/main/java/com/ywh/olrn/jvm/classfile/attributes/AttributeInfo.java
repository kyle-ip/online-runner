package com.ywh.olrn.jvm.classfile.attributes;

import com.ywh.olrn.jvm.classfile.ClassReader;
import com.ywh.olrn.jvm.classfile.constantPool.ConstantPool;

/**
 * attribute_info {
 *     u2 attribute_name_index;
 *     u4 attribute_length;
 *     u1 info[attribute_length];
 * }
 *
 * @author ywh
 * @since 21/07/2020
 */
public interface AttributeInfo {

    void readInfo(ClassReader reader);

    /**
     * 读取属性
     *
     * @param reader
     * @param cp
     * @return
     */
    static AttributeInfo[] readAttributes(ClassReader reader, ConstantPool cp) {
        int attributesCount = reader.nextU2ToInt();
        AttributeInfo[] attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            int attrNameIndex = reader.nextU2ToInt();
            String attrName = cp.getUTF8(attrNameIndex);
            long attrLen = reader.nextU4ToInt();
            AttributeInfo attributeInfo = newAttributeInfo(attrName, attrLen, cp);
            attributeInfo.readInfo(reader);
            attributes[i] = attributeInfo;
        }
        return attributes;
    }

    /**
     * TODO 工厂方法 xml 配置?
     *
     * @param attrName
     * @param attrLen
     * @param cp
     * @return
     */
    static AttributeInfo newAttributeInfo(String attrName, long attrLen, ConstantPool cp) {
        switch (attrName) {
            // method 属性：方法体
            case "Code":
                return new CodeAttribute(cp);
            // field 属性：常量表达式的值，存常量池索引
            case "ConstantValue":
                return new ConstantValueAttribute();
            // method 属性：变长属性，记录方法抛出的异常表
            case "Exceptions":
                return new ExceptionsAttribute();
            // 调试信息，不一定需要，使用 javac 编译器编译程序时，默认会在 class 文件中生成这些信息
            // method 属性的 Code 属性的属性
            // 方法行号
            case "LineNumberTable":
                return new LineNumberTableAttribute();
            // 方法局部变量
            case "LocalVariableTable":
                return new LocalVariableTableAttribute();
            // class 属性：源文件名，如 XXX.java
            case "SourceFile":
                return new SourceFileAttribute(cp);
            // 最简单的两种属性，仅起标记作用，不包含任何数据。
            // ClassFile、field_info 和 method_info都可以用
            // 嵌套类和嵌套接口支持
            case "Synthetic":
                return new SyntheticAttribute();
            // 废弃标记
            case "Deprecated":
                return new DeprecatedAttribute();
            // 不支持
            default:
                return new UnparsedAttribute(attrName, (int) attrLen);
        }
    }

}
