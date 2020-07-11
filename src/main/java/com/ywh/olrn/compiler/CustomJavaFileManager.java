package com.ywh.olrn.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义 JavaFileObject 管理器
 *
 * @author ywh
 * @since 11/07/2020
 */
public class CustomJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private static final Map<String, JavaFileObject> JAVA_FILE_OBJECT_MAP = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    public CustomJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

//
//    @Override
//    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
//        JavaFileObject javaFileObject = JAVA_FILE_OBJECT_MAP.get(className);
//        if (javaFileObject == null) {
//            return super.getJavaFileForInput(location, className, kind);
//        }
//        return javaFileObject;
//    }

    /**
     * 获取 JavaFileObject 对象（存储编译输出字节码）
     *
     * @param location
     * @param className
     * @param kind
     * @param sibling
     * @return
     */
    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        JavaFileObject javaFileObject = new CustomJavaFileObject(className, kind);
        JAVA_FILE_OBJECT_MAP.put(className, javaFileObject);
        return javaFileObject;
    }

    byte[] getJavaFileForInput(String className) {
        JavaFileObject javaFileObject = JAVA_FILE_OBJECT_MAP.get(className);
        return ((CustomJavaFileObject) javaFileObject).getByteArrayOutputStream().toByteArray();
    }
}
