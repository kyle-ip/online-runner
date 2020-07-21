package com.ywh.olrn.jvm.classfile.constantPool;

import lombok.Getter;
import com.ywh.olrn.jvm.classfile.ClassReader;


/**
 *
 * @author ywh
 * @since 21/07/2020
 */
@Getter
public class ConstantPool {

    /**
     * 常量数量
     */
    private final int constantPoolSize;

    /**
     * 常量
     */
    private ConstantInfo[] constantInfos;

    public ConstantPool(ClassReader reader) {
        constantPoolSize = reader.nextU2ToInt();
        constantInfos = new ConstantInfo[constantPoolSize];
        // 从 1 开始
        for (int i = 1; i < constantPoolSize; i++) {
            int tag = reader.nextU1toInt();
            ConstantInfo constInfo = ConstantInfo.createConstantInfo(tag, reader, this);
            constantInfos[i] = constInfo;
            if (tag == ConstantInfo.CONST_TAG_DOUBLE || tag == ConstantInfo.CONST_TAG_LONG) {
                i++;
            }
        }
    }

    public String getUTF8(int index) {
        ConstantInfo constInfo = this.constantInfos[index];
        return constInfo == null ? "" : constInfo.toString();
    }

}
