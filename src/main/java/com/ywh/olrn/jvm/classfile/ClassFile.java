package com.ywh.olrn.jvm.classfile;

import com.ywh.olrn.compiler.SourceCodeCompiler;
import com.ywh.olrn.jvm.classfile.attribute.*;
import com.ywh.olrn.jvm.classfile.attribute.LocalVariableTable.LocalVariable;
import com.ywh.olrn.jvm.classfile.attribute.LocalVariableTypeTable.LocalVariableType;
import com.ywh.olrn.jvm.classfile.attribute.RuntimeVisibleParameterAnnotations.ParameterAnnotation;
import com.ywh.olrn.jvm.classfile.attribute.code.ExceptionTable;
import com.ywh.olrn.jvm.classfile.attribute.innerClasses.Classes;
import com.ywh.olrn.jvm.classfile.attribute.lineNumberTable.LineNumber;
import com.ywh.olrn.jvm.classfile.attribute.runtimeVisibleAnnotations.*;
import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.*;
import com.ywh.olrn.jvm.classfile.attribute.stackmaptable.verificationtypeinfo.*;
import com.ywh.olrn.jvm.classfile.basestruct.U1;
import com.ywh.olrn.jvm.classfile.basestruct.U2;
import com.ywh.olrn.jvm.classfile.basestruct.U4;
import com.ywh.olrn.jvm.classfile.constantpool.*;
import com.ywh.olrn.jvm.classpath.BaseEntry;
import com.ywh.olrn.util.TypeUtils;

import java.util.Arrays;

import static com.ywh.olrn.util.TypeUtils.*;

/**
 * @author ywh
 * @since 20/07/2020
 */
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
public class ClassFile {

    /**
     * 魔数
     */
    public U4 magic;

    /**
     * 次版本号
     */
    public U2 minorVersion;

    /**
     * 主版本号
     */
    public U2 majorVersion;

    /**
     * 常量数量
     */
    public U2 constantPoolCount;

    /**
     * 常量池
     */
    public ConstantPool constantPool = new ConstantPool();

    /**
     * 类访问标志
     */
    public U2 accessFlags;

    /**
     * 类索引
     */
    public U2 thisClass;

    /**
     * 超类索引
     */
    public U2 superClass;

    /**
     * 接口数量
     */
    public U2 interfaceCount;

    /**
     * 接口索引表
     */
    public U2[] interfaces;

    /**
     * 字段数量
     */
    public U2 fieldCount;

    /**
     * 字段表
     */
    public FieldInfo[] fields;

    /**
     * 方法数量
     */
    public U2 methodsCount;

    /**
     * 方法表
     */
    public MethodInfo[] methods;

    /**
     * 属性数量
     */
    public U2 attributesCount;

    /**
     * 属性表
     */
    public AttributeBase[] attributes;

    /**
     * 字节码
     */
    private byte[] byteCode;

    /**
     * 访问索引
     */
    private int index;

    public ClassFile(byte[] byteCode) {
        this.byteCode = byteCode;
        this.index = 0;
        processByteCode(byteCode);
    }

    /**
     * 解析字节码为 ClassFile 对象
     *
     * @param byteCode
     */
    public void processByteCode(byte[] byteCode) {
        this.byteCode = byteCode;

        // 魔数
        magic = readU4();

        // 次版本号
        minorVersion = readU2();

        // 主版本号
        majorVersion = readU2();

        // 常量池
        constantPoolCount = readU2();
        processConstantPool();

        // 访问标记
        accessFlags = readU2();

        // 类索引
        thisClass = readU2();

        // 超类索引
        superClass = readU2();

        // 接口索引表
        interfaceCount = readU2();
        int interfaceCountInteger = TypeUtils.byteArr2Int(interfaceCount.u2);
        interfaces = new U2[interfaceCountInteger];
        for (int i = 0; i < interfaceCountInteger; i++) {
            interfaces[i] = readU2();
        }

        // 字段表
        fieldCount = readU2();
        int fieldCountInteger = TypeUtils.byteArr2Int(fieldCount.u2);
        fields = processFields(fieldCountInteger);

        // 方法表
        methodsCount = readU2();
        int methodsCountInteger = TypeUtils.byteArr2Int(methodsCount.u2);
        methods = processMethods(methodsCountInteger);

        // 属性表
        attributesCount = readU2();
        int tempAttributesCount = TypeUtils.byteArr2Int(attributesCount.u2);
        attributes = new AttributeBase[tempAttributesCount];
        for (int i = 0; i < tempAttributesCount; i++) {
            processAttribute(i, attributes);
        }
    }

    /**
     * 解析字节码中的方法
     *
     * @param methodsCountInteger
     * @return
     */
    private MethodInfo[] processMethods(int methodsCountInteger) {
        MethodInfo[] methodInfos = new MethodInfo[methodsCountInteger];
        for (int i = 0; i < methodsCountInteger; i++) {
            methodInfos[i] = new MethodInfo();
            methodInfos[i].accessFlags = readU2();
            methodInfos[i].nameIndex = readU2();
            methodInfos[i].descriptorIndex = readU2();
            methodInfos[i].attributeCount = readU2();
            int tempAttributesCount = TypeUtils.byteArr2Int(methodInfos[i].attributeCount.u2);
            methodInfos[i].attributes = new AttributeBase[tempAttributesCount];
            for (int j = 0; j < tempAttributesCount; j++) {
                processAttribute(j, methodInfos[i].attributes);
            }
        }
        return methodInfos;
    }

    private FieldInfo[] processFields(int fieldCountInteger) {
        FieldInfo[] fieldInfos = new FieldInfo[fieldCountInteger];
        for (int i = 0; i < fieldCountInteger; i++) {
            fieldInfos[i] = new FieldInfo();
            fieldInfos[i].accessFlags = readU2();
            fieldInfos[i].nameIndex = readU2();
            fieldInfos[i].descriptorIndex = readU2();
            fieldInfos[i].attributeCount = readU2();
            int tempAttributesCount = TypeUtils.byteArr2Int(fieldInfos[i].attributeCount.u2);
            fieldInfos[i].attributes = new AttributeBase[tempAttributesCount];
            for (int j = 0; j < tempAttributesCount; j++) {
                processAttribute(j, fieldInfos[i].attributes);
            }
        }
        return fieldInfos;
    }

    private void processConstantPool() {
        int poolSize = TypeUtils.byteArr2Int(constantPoolCount.u2);
        constantPool.cpInfo = new ConstantInfo[poolSize];
        for (int i = 0; i < poolSize - 1; i++) {
            U1 tag = readU1();

            int integerTag = TypeUtils.byteArr2Int(tag.u1);
            if (integerTag == 1) {
                ConstantUtf8 constantUtf8 = new ConstantUtf8();
                constantUtf8.tag = tag;
                constantUtf8.length = readU2();
                int utf8Len = TypeUtils.byteArr2Int(constantUtf8.length.u2);
                constantUtf8.bytes = new U1[utf8Len];
                for (int j = 0; j < utf8Len; j++) {
                    constantUtf8.bytes[j] = readU1();
                }
                constantPool.cpInfo[i] = constantUtf8;
            } else if (integerTag == 3) {
                ConstantInteger constantInteger = new ConstantInteger();
                constantInteger.tag = tag;
                constantInteger.bytes = readU4();
                constantPool.cpInfo[i] = constantInteger;
            } else if (integerTag == 4) {
                ConstantFloat constantFloat = new ConstantFloat();
                constantFloat.tag = tag;
                constantFloat.bytes = readU4();
                constantPool.cpInfo[i] = constantFloat;
            } else if (integerTag == 5) {
                ConstantLong constantLong = new ConstantLong();
                constantLong.tag = tag;
                constantLong.highBytes = readU4();
                constantLong.lowBytes = readU4();
                /*double和long类型会跳过一个常量标识*/
                constantPool.cpInfo[i++] = constantLong;
            } else if (integerTag == 6) {
                ConstantDouble constantDouble = new ConstantDouble();
                constantDouble.tag = tag;
                constantDouble.highBytes = readU4();
                constantDouble.lowBytes = readU4();
                /*double和long类型会跳过一个常量标识*/
                constantPool.cpInfo[i++] = constantDouble;
            } else if (integerTag == 7) {
                ConstantClass constantClass = new ConstantClass();
                constantClass.tag = tag;
                constantClass.nameIndex = readU2();
                constantPool.cpInfo[i] = constantClass;
            } else if (integerTag == 8) {
                ConstantString constantString = new ConstantString();
                constantString.tag = tag;
                constantString.stringIndex = readU2();
                constantPool.cpInfo[i] = constantString;
            } else if (integerTag == 9) {
                ConstantFieldref constantFieldref = new ConstantFieldref();
                constantFieldref.tag = tag;
                constantFieldref.classIndex = readU2();
                constantFieldref.nameAndTypeIndex = readU2();
                constantPool.cpInfo[i] = constantFieldref;
            } else if (integerTag == 10) {
                ConstantMethodref constantMethodref = new ConstantMethodref();
                constantMethodref.tag = tag;
                constantMethodref.classIndex = readU2();
                constantMethodref.nameAndTypeIndex = readU2();
                constantPool.cpInfo[i] = constantMethodref;
            } else if (integerTag == 11) {
                ConstantInterfacemethodref constantInterfaceMethodref = new ConstantInterfacemethodref();
                constantInterfaceMethodref.tag = tag;
                constantInterfaceMethodref.classIndex = readU2();
                constantInterfaceMethodref.nameAndTypeIndex = readU2();
                constantPool.cpInfo[i] = constantInterfaceMethodref;
            } else if (integerTag == 12) {
                ConstantNameandtype constantNameAndType = new ConstantNameandtype();
                constantNameAndType.tag = tag;
                constantNameAndType.nameIndex = readU2();
                constantNameAndType.descriptorIndex = readU2();
                constantPool.cpInfo[i] = constantNameAndType;
            } else if (integerTag == 15) {
                ConstantMethodhandle constantMethodHandle = new ConstantMethodhandle();
                constantMethodHandle.tag = tag;
                constantMethodHandle.referenceKind = readU1();
                constantMethodHandle.referenceIndex = readU2();
                constantPool.cpInfo[i] = constantMethodHandle;
            } else if (integerTag == 16) {
                ConstantMethodtype constantMethodType = new ConstantMethodtype();
                constantMethodType.tag = tag;
                constantMethodType.descriptorIndex = readU2();
                constantPool.cpInfo[i] = constantMethodType;
                //}else if(integer_tag == 17){
            } else if (integerTag == 18) {
                ConstantInvokedynamic constantInvokeDynamic = new ConstantInvokedynamic();
                constantInvokeDynamic.tag = tag;
                constantInvokeDynamic.bootstrapMethodAttrIndex = readU2();
                constantInvokeDynamic.nameAndTypeIndex = readU2();
                constantPool.cpInfo[i] = constantInvokeDynamic;
            } else {
                System.out.println("constantPool integer_tag " + integerTag);
                return;
            }
        }
    }

    /**
     * 属性类型
     */
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    public static String[] attributeStrs = {
        "ConstantValue",
        "Code",
        "StackMapTable",
        "Exceptions",
        "InnerClasses",
        "EnclosingMethod",
        "Synthetic",
        "Signature",
        "SourceFile",
        "SourceDebugExtension",
        "LineNumberTable",
        "LocalVariableTable",
        "LocalVariableTypeTable",
        "Deprecated",
        "RuntimeVisibleAnnotations",
        "RuntimeInvisibleAnnotations",
        "RuntimeVisibleParameterAnnotations",
        "RuntimeInvisibleParameterAnnotations",
        /*java8增加*/
        "RuntimeVisibleTypeAnnotations",
        /*java8增加*/
        "RuntimeInvisibleTypeAnnotations",
        "AnnotationDefault",
        "BootstrapMethods",
        /*java8增加*/
        "MethodParameters"
    };

    /**
     * 解析字节码中的属性
     *
     * @param index
     * @param attributes
     */
    public void processAttribute(int index, AttributeBase[] attributes) {
        U2 attributesNameIndex = readU2();
        int tempIndex = TypeUtils.byteArr2Int(attributesNameIndex.u2);
        ConstantInfo constantInfo = constantPool.cpInfo[tempIndex - 1];
        ConstantUtf8 constantUtf8 = (ConstantUtf8) constantInfo;
        if (constantUtf8.tag.u1[0] != 0x1) {
            return;
        }

        String s = TypeUtils.u12String(constantUtf8.bytes);
        if (TypeUtils.compare(s, attributeStrs[0])) {
            ConstantValueAttribute constantValue = new ConstantValueAttribute();
            constantValue.attributeNameIndex = attributesNameIndex;
            /*恒等于2*/
            constantValue.attributeLength = readU4();
            constantValue.constantvalueIndex = readU2();
            attributes[index] = constantValue;
        } else if (TypeUtils.compare(s, attributeStrs[1])) {
            CodeAttribute codeAttribute = new CodeAttribute();
            codeAttribute.attributeNameIndex = attributesNameIndex;
            codeAttribute.attributeLength = readU4();
            codeAttribute.maxStack = readU2();
            codeAttribute.maxLocals = readU2();
            codeAttribute.codeLength = readU4();
            int len = TypeUtils.byteArr2Int(codeAttribute.codeLength.u4);
            codeAttribute.code = new U1[len];
            for (int i = 0; i < len; i++) {
                codeAttribute.code[i] = readU1();
            }
            codeAttribute.exceptionTableLength = readU2();
            len = TypeUtils.byteArr2Int(codeAttribute.exceptionTableLength.u2);
            codeAttribute.exceptionTables = new ExceptionTable[len];
            for (int i = 0; i < len; i++) {
                codeAttribute.exceptionTables[i] = new ExceptionTable();
                codeAttribute.exceptionTables[i].startPc = readU2();
                codeAttribute.exceptionTables[i].endPc = readU2();
                codeAttribute.exceptionTables[i].handlerPc = readU2();
                codeAttribute.exceptionTables[i].catchType = readU2();
            }
            codeAttribute.attributeCount = readU2();
            int tempAttributesCount = TypeUtils.byteArr2Int(codeAttribute.attributeCount.u2);
            codeAttribute.attributes = new AttributeBase[tempAttributesCount];
            for (int i = 0; i < tempAttributesCount; i++) {

                processAttribute(i, codeAttribute.attributes);
            }
            attributes[index] = codeAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[2])) {
            /* https://hllvm-group.iteye.com/group/topic/26545 */
            StackMapTableAttribute stackMapTableAttribute = new StackMapTableAttribute();
            stackMapTableAttribute.attributeNameIndex = attributesNameIndex;
            processStackMapTable(stackMapTableAttribute);
            attributes[index] = stackMapTableAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[3])) {
            ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();
            exceptionsAttribute.attributeNameIndex = attributesNameIndex;
            exceptionsAttribute.attributeLength = readU4();
            exceptionsAttribute.numberOfExceptions = readU2();
            int exceptionSize = TypeUtils.byteArr2Int(exceptionsAttribute.numberOfExceptions.u2);
            exceptionsAttribute.exceptionIndexTable = new U2[exceptionSize];
            for (int j = 0; j < exceptionSize; j++) {
                exceptionsAttribute.exceptionIndexTable[j] = readU2();
            }
            attributes[index] = exceptionsAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[4])) {
            InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute();
            innerClassesAttribute.attributeNameIndex = attributesNameIndex;
            innerClassesAttribute.attributeLength = readU4();
            innerClassesAttribute.numberOfClasses = readU2();
            int classesSize = TypeUtils.byteArr2Int(innerClassesAttribute.numberOfClasses.u2);
            innerClassesAttribute.classes = new Classes[classesSize];
            for (int j = 0; j < classesSize; j++) {
                innerClassesAttribute.classes[j] = new Classes();
                innerClassesAttribute.classes[j].innerClassInfoIndex = readU2();
                innerClassesAttribute.classes[j].outerClassInfoIndex = readU2();
                innerClassesAttribute.classes[j].innerNameIndex = readU2();
                innerClassesAttribute.classes[j].innerClassAccessFlags = readU2();
            }
            attributes[index] = innerClassesAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[5])) {
            EnclosingMethodAttribute enclosingMethodAttribute = new EnclosingMethodAttribute();
            enclosingMethodAttribute.attributeNameIndex = attributesNameIndex;
            enclosingMethodAttribute.attributeLength = readU4();
            enclosingMethodAttribute.classIndex = readU2();
            enclosingMethodAttribute.classIndex = readU2();
            attributes[index] = enclosingMethodAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[6])) {
            SyntheticAttribute syntheticAttribute = new SyntheticAttribute();
            syntheticAttribute.attributeNameIndex = attributesNameIndex;
            syntheticAttribute.attributeLength = readU4();
            attributes[index] = syntheticAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[7])) {
            SignatureAttribute signatureAttribute = new SignatureAttribute();
            signatureAttribute.attributeNameIndex = attributesNameIndex;
            signatureAttribute.attributeLength = readU4();
            signatureAttribute.signatureIndex = readU2();
            attributes[index] = signatureAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[8])) {
            SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();
            sourceFileAttribute.attributeNameIndex = attributesNameIndex;
            sourceFileAttribute.attributeLength = readU4();
            sourceFileAttribute.sourcefileIndex = readU2();
            attributes[index] = sourceFileAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[9])) {
            SourceDebugExtensionAttribute sourceDebugExtensionAttribute = new SourceDebugExtensionAttribute();
            sourceDebugExtensionAttribute.attributeNameIndex = attributesNameIndex;
            sourceDebugExtensionAttribute.attributeLength = readU4();
            int debugSize = TypeUtils.byteArr2Int(sourceDebugExtensionAttribute.attributeLength.u4);
            sourceDebugExtensionAttribute.debugExtension = new U1[debugSize];
            for (int j = 0; j < debugSize; j++) {
                sourceDebugExtensionAttribute.debugExtension[j] = readU1();
            }
            attributes[index] = sourceDebugExtensionAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[10])) {
            LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();
            lineNumberTableAttribute.attributeNameIndex = attributesNameIndex;
            lineNumberTableAttribute.attributeLength = readU4();
            lineNumberTableAttribute.lineNumberTableLength = readU2();
            int lineNumberSize = TypeUtils.byteArr2Int(lineNumberTableAttribute.lineNumberTableLength.u2);
            lineNumberTableAttribute.lineNumbers = new LineNumber[lineNumberSize];
            for (int j = 0; j < lineNumberSize; j++) {
                lineNumberTableAttribute.lineNumbers[j] = new LineNumber();
                lineNumberTableAttribute.lineNumbers[j].startPc = readU2();
                lineNumberTableAttribute.lineNumbers[j].lineNumber = readU2();
            }
            attributes[index] = lineNumberTableAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[11])) {
            LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();
            localVariableTableAttribute.attributeNameIndex = attributesNameIndex;
            localVariableTableAttribute.attributeLength = readU4();
            localVariableTableAttribute.localVariableTableLength = readU2();
            int localVariableSize = TypeUtils.byteArr2Int(localVariableTableAttribute.localVariableTableLength.u2);
            localVariableTableAttribute.localVariables = new LocalVariable[localVariableSize];
            for (int j = 0; j < localVariableSize; j++) {
                LocalVariable localVariable = new LocalVariable();
                localVariable.startPc = readU2();
                localVariable.length = readU2();
                localVariable.nameIndex = readU2();
                localVariable.descriptorIndex = readU2();
                localVariable.index = readU2();
                localVariableTableAttribute.localVariables[j] = localVariable;
            }
            attributes[index] = localVariableTableAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[12])) {
            LocalVariableTypeTableAttribute localVariableTypeTable = new LocalVariableTypeTableAttribute();
            localVariableTypeTable.attributeNameIndex = attributesNameIndex;
            localVariableTypeTable.attributeLength = readU4();
            localVariableTypeTable.localVariableTypeTableLength = readU2();
            int localVariableTypeSize =
                TypeUtils.byteArr2Int(localVariableTypeTable.localVariableTypeTableLength.u2);
            localVariableTypeTable.localVariableTypes = new LocalVariableType[localVariableTypeSize];
            for (int j = 0; j < localVariableTypeSize; j++) {
                LocalVariableType localVariableType = new LocalVariableType();
                localVariableType.startPc = readU2();
                localVariableType.length = readU2();
                localVariableType.nameIndex = readU2();
                localVariableType.signatureIndex = readU2();
                localVariableType.index = readU2();
                localVariableTypeTable.localVariableTypes[j] = localVariableType;
            }
            attributes[index] = localVariableTypeTable;
        } else if (TypeUtils.compare(s, attributeStrs[13])) {
            DeprecatedAttribute deprecatedAttribute = new DeprecatedAttribute();
            deprecatedAttribute.attributeNameIndex = attributesNameIndex;
            deprecatedAttribute.attributeLength = readU4();
            attributes[index] = deprecatedAttribute;
        } else if (TypeUtils.compare(s, attributeStrs[14])) {
            RuntimeVisibleAnnotationsAttribute runtimeVisibleAnnotations = new RuntimeVisibleAnnotationsAttribute();
            runtimeVisibleAnnotations.attributeNameIndex = attributesNameIndex;
            runtimeVisibleAnnotations.attributeLength = readU4();
            runtimeVisibleAnnotations.numAnnotations = readU2();
            int annotationsSize = TypeUtils.byteArr2Int(runtimeVisibleAnnotations.numAnnotations.u2);
            runtimeVisibleAnnotations.annotations = new Annotation[annotationsSize];
            for (int j = 0; j < annotationsSize; j++) {
                Annotation annotation = runtimeVisibleAnnotations.annotations[j] = new Annotation();
                processAnnotation(annotation);
            }
            attributes[index] = runtimeVisibleAnnotations;
        } else if (TypeUtils.compare(s, attributeStrs[15])) {
            RuntimeInvisibleAnnotationsAttribute runtimeInvisibleAnnotations =
                new RuntimeInvisibleAnnotationsAttribute();
            runtimeInvisibleAnnotations.attributeNameIndex = attributesNameIndex;
            runtimeInvisibleAnnotations.attributeLength = readU4();
            runtimeInvisibleAnnotations.numAnnotations = readU2();
            int annotationsSize = TypeUtils.byteArr2Int(runtimeInvisibleAnnotations.numAnnotations.u2);
            for (int j = 0; j < annotationsSize; j++) {
                Annotation annotation = runtimeInvisibleAnnotations.annotations[j];
                processAnnotation(annotation);
            }
            attributes[index] = runtimeInvisibleAnnotations;
        } else if (TypeUtils.compare(s, attributeStrs[16])) {
            RuntimeVisibleParameterAnnotationsAttribute runtimeVisibleParameterAnnotations =
                new RuntimeVisibleParameterAnnotationsAttribute();
            runtimeVisibleParameterAnnotations.attributeNameIndex = attributesNameIndex;
            runtimeVisibleParameterAnnotations.attributeLength = readU4();
            runtimeVisibleParameterAnnotations.numParameters = readU1();
            int parametersSize = TypeUtils.byteArr2Int(runtimeVisibleParameterAnnotations.numParameters.u1);
            for (int j = 0; j < parametersSize; j++) {
                ParameterAnnotation parameterAnnotation = runtimeVisibleParameterAnnotations.parameterAnnotations[j];
                processParameterAnnotation(parameterAnnotation);
            }
            attributes[index] = runtimeVisibleParameterAnnotations;
        } else if (TypeUtils.compare(s, attributeStrs[17])) {
            RuntimeInvisibleParameterAnnotationsAttribute runtimeInvisibleParameterAnnotations =
                new RuntimeInvisibleParameterAnnotationsAttribute();
            runtimeInvisibleParameterAnnotations.attributeNameIndex = attributesNameIndex;
            runtimeInvisibleParameterAnnotations.attributeLength = readU4();
            runtimeInvisibleParameterAnnotations.numParameters = readU1();
            int parametersSize = TypeUtils.byteArr2Int(runtimeInvisibleParameterAnnotations.numParameters.u1);
            for (int j = 0; j < parametersSize; j++) {
                ParameterAnnotation parameterAnnotation = runtimeInvisibleParameterAnnotations.parameterAnnotations[j];
                processParameterAnnotation(parameterAnnotation);
            }
            attributes[index] = runtimeInvisibleParameterAnnotations;

            /*java8增加 RuntimeVisibleTypeAnnotationsAttribute*/
        } else if (TypeUtils.compare(s, attributeStrs[18])) {

            /*java8增加 RuntimeInvisibleTypeAnnotationsAttribute*/
        } else if (TypeUtils.compare(s, attributeStrs[19])) {

        } else if (TypeUtils.compare(s, attributeStrs[20])) {
            AnnotationDefaultAttribute annotationDefault = new AnnotationDefaultAttribute();
            annotationDefault.attributeNameIndex = attributesNameIndex;
            annotationDefault.attributeLength = readU4();
            annotationDefault.defaultValue = processElementValue();
            attributes[index] = annotationDefault;
        } else if (TypeUtils.compare(s, attributeStrs[21])) {
            BootstrapMethodsAttribute bootstrapMethods = new BootstrapMethodsAttribute();
            bootstrapMethods.attributeNameIndex = attributesNameIndex;
            bootstrapMethods.attributeLength = readU4();
            bootstrapMethods.numBootstrapMethods = readU2();
            int bootstrapMethodsSize = TypeUtils.byteArr2Int(bootstrapMethods.numBootstrapMethods.u2);
            bootstrapMethods.bootstrapMethods = new BootstrapMethod[bootstrapMethodsSize];
            for (int j = 0; j < bootstrapMethodsSize; j++) {
                BootstrapMethod bootstrapMethod = new BootstrapMethod();
                bootstrapMethod.bootstrapMethodRef = readU2();
                bootstrapMethod.numBootstrapArguments = readU2();
                int argumentsSize = TypeUtils.byteArr2Int(bootstrapMethod.numBootstrapArguments.u2);
                bootstrapMethod.bootstrapArguments = new U2[argumentsSize];
                for (int k = 0; k < argumentsSize; k++) {
                    bootstrapMethod.bootstrapArguments[k] = readU2();
                }
                bootstrapMethods.bootstrapMethods[j] = bootstrapMethod;
            }
            attributes[index] = bootstrapMethods;

            /*java8增加 MethodParametersAttribute*/
        } else if (TypeUtils.compare(s, attributeStrs[22])) {

        }
    }

    void processParameterAnnotation(ParameterAnnotation parameterAnnotation) {
        parameterAnnotation.numAnnotations = readU2();
        int annotationsSize = TypeUtils.byteArr2Int(parameterAnnotation.numAnnotations.u2);
        parameterAnnotation.annotations = new Annotation[annotationsSize];
        for (int k = 0; k < annotationsSize; k++) {
            parameterAnnotation.annotations[k] = new Annotation();
            processAnnotation(parameterAnnotation.annotations[k]);
        }
    }

    void processAnnotation(Annotation annotation) {
        annotation.typeIndex = readU2();
        annotation.numElementValuePairs = readU2();
        int pairsSize = TypeUtils.byteArr2Int(annotation.numElementValuePairs.u2);
        annotation.elementValuePairs = new ElementValuePair[pairsSize];
        for (int k = 0; k < pairsSize; k++) {
            annotation.elementValuePairs[k] = new ElementValuePair();
            processElementValuePair(annotation.elementValuePairs[k]);
        }
    }

    void processElementValuePair(ElementValuePair elementValuePair) {
        elementValuePair.elementNameIndex = readU2();
        elementValuePair.value = processElementValue();
    }

    ElementValue processElementValue() {
        ElementValue elementValue = null;
        U1 elementValueTag = readU1();
        int tagInteger = TypeUtils.byteArr2Int(elementValueTag.u1);

        if (Integer.valueOf('B').equals(tagInteger)
            || Integer.valueOf('C').equals(tagInteger)
            || Integer.valueOf('D').equals(tagInteger)
            || Integer.valueOf('F').equals(tagInteger)
            || Integer.valueOf('I').equals(tagInteger)
            || Integer.valueOf('J').equals(tagInteger)
            || Integer.valueOf('S').equals(tagInteger)
            || Integer.valueOf('Z').equals(tagInteger)
            || Integer.valueOf('s').equals(tagInteger)) {
            ConstValueIndex constValueIndex = new ConstValueIndex();
            constValueIndex.tag = elementValueTag;
            constValueIndex.constValueIndex = readU2();
            elementValue = constValueIndex;
        } else if (Integer.valueOf('e').equals(tagInteger)) {
            EnumConstValue enumConstValue = new EnumConstValue();
            enumConstValue.tag = elementValueTag;
            enumConstValue.typeNameIndex = readU2();
            enumConstValue.constNameIndex = readU2();
            elementValue = enumConstValue;
        } else if (Integer.valueOf('c').equals(tagInteger)) {
            ClassInfoIndex classInfoIndex = new ClassInfoIndex();
            classInfoIndex.tag = elementValueTag;
            classInfoIndex.classInfoIndex = readU2();
            elementValue = classInfoIndex;
        } else if (Integer.valueOf('@').equals(tagInteger)) {
            ValueAnnotation valueAnnotation = new ValueAnnotation();
            valueAnnotation.tag = elementValueTag;
            valueAnnotation.annotationValue = new Annotation();
            processAnnotation(valueAnnotation.annotationValue);
            elementValue = valueAnnotation;
        } else if (Integer.valueOf('[').equals(tagInteger)) {
            ArrayValue arrayValue = new ArrayValue();
            arrayValue.tag = elementValueTag;
            arrayValue.numValues = readU2();
            int numValueInteger = TypeUtils.byteArr2Int(arrayValue.numValues.u2);
            ElementValue[] elementValues = new ElementValue[numValueInteger];
            for (int i = 0; i < numValueInteger; i++) {
                elementValues[i] = processElementValue();
            }
            arrayValue.values = elementValues;
            elementValue = arrayValue;
        }
        return elementValue;

    }

    void processStackMapTable(StackMapTableAttribute stackMapTableAttribute) {
        stackMapTableAttribute.attributeLength = readU4();
        stackMapTableAttribute.numberOfEntries = readU2();
        int len = TypeUtils.byteArr2Int(stackMapTableAttribute.numberOfEntries.u2);
        stackMapTableAttribute.entries = new StackMapFrame[len];
        for (int i = 0; i < len; i++) {
            // stackMapTable_attribute.entries[i];
            U1 frameTag = readU1();
            int frameTagInteger = TypeUtils.byteArr2Int(frameTag.u1);
            /* 128 至 246是预留的*/
            /*SameFrame */
            if (frameTagInteger >= 0 && frameTagInteger <= 63) {
                SameFrame sameFrame = new SameFrame();
                sameFrame.frameType = frameTag;
                stackMapTableAttribute.entries[i] = sameFrame;

                /*SameLocals1StackItemFrame*/
            } else if (frameTagInteger >= 64 && frameTagInteger <= 127) {
                SameLocals1StackItemFrame sameLocals1StackItemFrame = new SameLocals1StackItemFrame();
                sameLocals1StackItemFrame.frameType = frameTag;
                sameLocals1StackItemFrame.stack = new VerificationTypeInfo[1];
                U1 verificationTypeTag = readU1();
                sameLocals1StackItemFrame.stack[0] = getVerificationTypeTag(verificationTypeTag);
                stackMapTableAttribute.entries[i] = sameLocals1StackItemFrame;

                /*SameLocals1StackItemFrameExtended*/
            } else if (frameTagInteger == 247) {
                SameLocals1StackItemFrameExtended sameLocals1StackItemFrameExtended =
                    new SameLocals1StackItemFrameExtended();
                sameLocals1StackItemFrameExtended.frameType = frameTag;
                sameLocals1StackItemFrameExtended.offsetDelta = readU2();
                sameLocals1StackItemFrameExtended.stack = new VerificationTypeInfo[1];
                U1 verificationTypeTag = readU1();
                sameLocals1StackItemFrameExtended.stack[0] = getVerificationTypeTag(verificationTypeTag);
                stackMapTableAttribute.entries[i] = sameLocals1StackItemFrameExtended;

                /*ChopFrame*/
            } else if (frameTagInteger >= 248 && frameTagInteger <= 250) {
                ChopFrame chopFrame = new ChopFrame();
                chopFrame.frameType = frameTag;
                chopFrame.offsetDelta = readU2();
                stackMapTableAttribute.entries[i] = chopFrame;

                /*SameFrameExtended*/
            } else if (frameTagInteger == 251) {
                SameFrameExtended sameFrameExtended = new SameFrameExtended();
                sameFrameExtended.frameType = frameTag;
                sameFrameExtended.offsetDelta = readU2();
                stackMapTableAttribute.entries[i] = sameFrameExtended;

                /*AppendFrame*/
            } else if (frameTagInteger >= 252 && frameTagInteger <= 254) {
                AppendFrame appendFrame = new AppendFrame();
                appendFrame.frameType = frameTag;
                appendFrame.offsetDelta = readU2();
                int localsSize = frameTagInteger - 251;
                appendFrame.locals = new VerificationTypeInfo[localsSize];
                for (int j = 0; j < localsSize; j++) {
                    U1 verificationTypeTag = readU1();
                    appendFrame.locals[j] = getVerificationTypeTag(verificationTypeTag);
                }
                stackMapTableAttribute.entries[i] = appendFrame;

                /*FullFrame*/
            } else if (frameTagInteger >= 255) {
                FullFrame fullFrame = new FullFrame();
                fullFrame.frameType = frameTag;
                fullFrame.offsetDelta = readU2();
                fullFrame.numberOfLocals = readU2();
                int localsSize = TypeUtils.byteArr2Int(fullFrame.numberOfLocals.u2);
                fullFrame.locals = new VerificationTypeInfo[localsSize];
                for (int j = 0; j < localsSize; j++) {
                    U1 verificationTypeTag = readU1();
                    fullFrame.locals[j] = getVerificationTypeTag(verificationTypeTag);
                }

                fullFrame.numberOfStackItems = readU2();
                int stackItemsSize = TypeUtils.byteArr2Int(fullFrame.numberOfStackItems.u2);
                fullFrame.stack = new VerificationTypeInfo[stackItemsSize];
                for (int j = 0; j < stackItemsSize; j++) {
                    U1 verificationTypeTag = readU1();
                    fullFrame.stack[j] = getVerificationTypeTag(verificationTypeTag);
                }

                stackMapTableAttribute.entries[i] = fullFrame;
            }
        }
    }

    VerificationTypeInfo getVerificationTypeTag(U1 tag) {
        int tagInteger = TypeUtils.byteArr2Int(tag.u1);
        if (tagInteger == 0) {
            TopVariableInfo topVariableInfo = new TopVariableInfo();
            topVariableInfo.tag = tag;
            return topVariableInfo;
        } else if (tagInteger == 1) {
            IntegerVariableInfo integerVariableInfo = new IntegerVariableInfo();
            integerVariableInfo.tag = tag;
            return integerVariableInfo;
        } else if (tagInteger == 2) {
            FloatVariableInfo floatVariableInfo = new FloatVariableInfo();
            floatVariableInfo.tag = tag;
            return floatVariableInfo;
        } else if (tagInteger == 4) {
            LongVariableInfo longVariableInfo = new LongVariableInfo();
            longVariableInfo.tag = tag;
            return longVariableInfo;
        } else if (tagInteger == 3) {
            DoubleVariableInfo doubleVariableInfo = new DoubleVariableInfo();
            doubleVariableInfo.tag = tag;
            return doubleVariableInfo;
        } else if (tagInteger == 5) {
            NullVariableInfo nullVariableInfo = new NullVariableInfo();
            nullVariableInfo.tag = tag;
            return nullVariableInfo;
        } else if (tagInteger == 6) {
            UninitializedThisVariableInfo uninitializedThisVariableInfo = new UninitializedThisVariableInfo();
            uninitializedThisVariableInfo.tag = tag;
            return uninitializedThisVariableInfo;
        } else if (tagInteger == 7) {
            ObjectVariableInfo objectVariableInfo = new ObjectVariableInfo();
            objectVariableInfo.tag = tag;
            objectVariableInfo.cpoolIndex = readU2();
            return objectVariableInfo;
        } else if (tagInteger == 8) {
            UninitializedVariableInfo uninitializedVariableInfo = new UninitializedVariableInfo();
            uninitializedVariableInfo.tag = tag;
            uninitializedVariableInfo.offset = readU2();
            return uninitializedVariableInfo;
        }
        return null;
    }

    enum TAG {
        CONSTANT_Utf8("ConstantUtf8", (byte) 0x1),
        CONSTANT_Integer("ConstantInteger", (byte) 0x3),
        CONSTANT_Float("ConstantFloat", (byte) 0x4),
        CONSTANT_Long("ConstantLong", (byte) 0x5),
        CONSTANT_Double("ConstantDouble", (byte) 0x6),
        CONSTANT_Class("ConstantClass", (byte) 0x7),
        CONSTANT_String("ConstantString", (byte) 0x8),
        CONSTANT_Fieldref("ConstantFieldref", (byte) 0x9),
        CONSTANT_Methodref("ConstantMethodref", (byte) 0x10),
        CONSTANT_InterfaceMethodref("ConstantInterfacemethodref", (byte) 0x11),
        CONSTANT_NameAndType("ConstantNameandtype", (byte) 0x12),
        CONSTANT_MethodHandle("ConstantMethodhandle", (byte) 0x15),
        CONSTANT_MethodType("ConstantMethodtype", (byte) 0x16),
        CONSTANT_InvokeDynamic("ConstantInvokedynamic", (byte) 0x17);

        public String name;
        public byte index;

        private TAG(String name, byte index) {
            this.name = name;
            this.index = index;
        }
    }


    public U1 readU1() {
        U1 res = new U1();
        res.u1 = read(1);
        index++;
        return res;
    }

    public U2 readU2() {
        U2 res = new U2();
        res.u2 = read(2);
        index += 2;
        return res;
    }

    public U4 readU4() {
        U4 res = new U4();
        res.u4 = read(4);
        index += 4;
        return res;
    }

    public byte[] read(int size) {
        byte[] bytes = new byte[size];
        if (size >= 0) {
            System.arraycopy(byteCode, index, bytes, 0, size);
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "ClassFile{" +
            "magic=" + bytes2HexString(magic.u4) +
            ", minorVersion=" + bytes2HexString(minorVersion.u2) +
            ", majorVersion=" + bytes2HexString(majorVersion.u2) +
            ", constantPoolCount=" + bytes2HexString(constantPoolCount.u2) +
            ", constantPool=" + constantPool.cpInfo +
            ", accessFlags=" + bytes2HexString(accessFlags.u2) +
            ", thisClass=" + bytes2HexString(thisClass.u2) +
            ", superClass=" + bytes2HexString(superClass.u2) +
            ", interfaceCount=" + bytes2HexString(interfaceCount.u2) +
            ", interfaces=" + Arrays.toString(interfaces) +
            ", fieldCount=" + bytes2HexString(fieldCount.u2) +
            ", fields=" + Arrays.toString(fields) +
            ", methodsCount=" + bytes2HexString(methodsCount.u2) +
            ", methods=" + Arrays.toString(methods) +
            ", attributesCount=" + bytes2HexString(attributesCount.u2) +
            ", attributes=" + Arrays.toString(attributes) +
            '}';
    }

    public static void main(String[] args) throws Exception {
        String sourceCode = ""
            + "public class Run {\n"
            + " private static final long serialVersionUID = -6849794470754667710L; \n"
            + "    public static void main(String[] args) {\n"
            + "        System.out.println(\"Hello, World!\");\n"
            + "    }\n"
            + "}";
        byte[] byteCode = SourceCodeCompiler.compile(sourceCode);
        ClassFile classFile = new ClassFile(byteCode);
        System.out.println(classFile);
    }
}