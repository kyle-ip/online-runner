package com.ywh.olrn.jvm.classfile;

import com.ywh.olrn.jvm.classfile.constantPool.ConstantPool;
import lombok.Getter;

/**
 * @author ywh
 * @since 21/07/2020
 */
@Getter
public class ClassFile {

    /**
     * 魔数 4byte
     */
    private static final String CAFEBABE = "cafebabe";

    //private static Long CAFEBABE = new BigInteger("cafebabe",16).longValue();

    //private static int ACC_PUBLIC =0x0021;

    /**
     * 次版本号 2byte
     */
    private int minorVersion;

    /**
     * 主版本号 4byte
     */
    private int majorVersion;

    /**
     * 常量池
     */
    private ConstantPool constantPool;

    /**
     * 访问标记
     */
    private int accessFlag;

    /**
     * 类索引
     */
    private int classNameIndex;

    /**
     * 超类索引 2bytes
     */
    private int superClassNameIndex;

    /**
     * 接口索引表
     */
    private int[] interfaceIndexes;

    /**
     * 字段表
     */
    private MemberInfo[] fields;

    /**
     * 方法表
     */
    private MemberInfo[] methods;

    private final ClassReader reader;

    public ClassFile(byte[] classData) {
        reader = new ClassReader(classData);

        this.readAndCheckMagic();
        this.readAndCheckVersion();
        this.readConstantPool();

        this.readAccessFlag();
        this.readClassNameIndex();
        this.readSuperClassNameIndex();
        this.readInterfaceIndexes();
        this.readFields();
        this.readMethods();
    }

    private void readAndCheckMagic() {
        String magic = this.reader.nextU4ToHexString();
        if (!CAFEBABE.equals(magic)) {
            throw new ClassFormatError("magic!");
        }
    }

    private void readAndCheckVersion() {
        this.minorVersion = this.reader.nextU2ToInt();
        this.majorVersion = this.reader.nextU2ToInt();
        if (this.majorVersion >= 46 && this.majorVersion <= 52 && this.minorVersion == 0) {
            return;
        }
        throw new UnsupportedClassVersionError();
    }

    private void readConstantPool() {
        this.constantPool = new ConstantPool(this.reader);
    }

    private void readAccessFlag() {
        accessFlag = reader.nextU2ToInt();
    }

    private void readClassNameIndex() {
        classNameIndex = reader.nextU2ToInt();
    }

    private void readSuperClassNameIndex() {
        superClassNameIndex = reader.nextU2ToInt();
    }

    private void readInterfaceIndexes() {
        interfaceIndexes = reader.nextUint16s();
    }

    private void readFields() {
        fields = MemberInfo.readMembers(constantPool, reader);
    }

    private void readMethods() {
        methods = MemberInfo.readMembers(constantPool, reader);
    }
}
