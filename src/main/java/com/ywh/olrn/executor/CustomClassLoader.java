package com.ywh.olrn.executor;

/**
 * 自定义类加载器
 * 每次新建一个类加载器用于加载从客户端传来类源码（热加载），否则已加载的类无法重新加载
 *
 * CustomClassLoader customClassLoader = new CustomClassLoader();
 * Class clazz = customClassLoader.loadByte(byteCode);
 *
 * @author ywh
 * @since 12/07/2020
 */
public class CustomClassLoader extends ClassLoader {

    public CustomClassLoader() {
        super(CustomClassLoader.class.getClassLoader());
    }

    public Class loadByte(byte[] classBytes) {
        return defineClass(null, classBytes, 0, classBytes.length);
    }
}
