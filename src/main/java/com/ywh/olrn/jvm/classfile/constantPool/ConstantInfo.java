package com.ywh.olrn.jvm.classfile.constantPool;

import com.ywh.olrn.jvm.classfile.ClassReader;

/**
 * 常量信息
 *
 * @author ywh
 * @since 21/07/2020
 */
public interface ConstantInfo {

    int CONST_TAG_CLASS = 7;

    int CONST_TAG_FIELD_REF = 9;

    int CONST_TAG_METHOD_REF = 10;

    int CONST_TAG_INTERFACE_MTTHOD_REF = 11;

    int CONST_TAG_STRING = 8;

    int CONST_TAG_INTEGER = 3;

    int CONST_TAG_FLOAT = 4;

    int CONST_TAG_LONG = 5;

    int CONST_TAG_DOUBLE = 6;

    int CONST_TAG_NAME_AND_TYPE = 12;

    int CONST_TAG_UTF8 = 1;

    int CONST_TAG_METHOD_HANDLE = 15;

    int CONST_TAG_METHOD_TYPE = 16;

    int CONST_TAG_INVOKE_DYNAMIC = 18;


    String getValue();

    /**
     *
     * @param tag
     * @param reader
     * @param constPool
     * @return
     */
    static ConstantInfo createConstantInfo(int tag, ClassReader reader, ConstantPool constPool) {
        switch (tag) {
            // 数值
            case CONST_TAG_INTEGER:
                return new ConstantIntegerInfo(reader);
            case CONST_TAG_FLOAT:
                return new ConstantFloatInfo(reader);
            case CONST_TAG_LONG:
                return new ConstantLongInfo(reader);
            case CONST_TAG_DOUBLE:
                return new ConstantDoubleInfo(reader);
            case CONST_TAG_UTF8:
                return new ConstantUft8Info(reader);

            // 引用指向 UTF8
            case CONST_TAG_STRING:
                return new ConstantStringInfo(constPool, reader);
            case CONST_TAG_CLASS:
                return new ConstantClassInfo(constPool, reader);
            case CONST_TAG_NAME_AND_TYPE:
                return new ConstantNameAndTypeInfo(constPool, reader);

            // 引用 CLASS+NAME_AND_TYPE
            case CONST_TAG_FIELD_REF:
            case CONST_TAG_METHOD_REF:
            case CONST_TAG_INTERFACE_MTTHOD_REF:
                return new ConstantMemberrefInfo(constPool, reader);

            // invokedynamic 支持（Java SE 7+）
            case CONST_TAG_METHOD_HANDLE:
            case CONST_TAG_METHOD_TYPE:
            case CONST_TAG_INVOKE_DYNAMIC:
                return null;

            default:
                throw new ClassFormatError("constant pool tag!");
        }
    }

}
