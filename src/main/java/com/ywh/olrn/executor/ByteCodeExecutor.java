package com.ywh.olrn.executor;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 字节码执行器
 *
 * @author ywh
 * @since 12/07/2020
 */
@Slf4j
public class ByteCodeExecutor {

    private static final int TIMEOUT = 15;

    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    private static final ExecutorService THREAD_POOL_EXECUTOR =
        new ThreadPoolExecutor(AVAILABLE_PROCESSORS + 1,
            AVAILABLE_PROCESSORS + 1,
            0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(AVAILABLE_PROCESSORS + 1));

    /**
     * @param byteCode
     * @param systemIn
     * @return
     */
    public static String execute(byte[] byteCode, String systemIn) {

        Future<String> future = THREAD_POOL_EXECUTOR.submit(() -> run(byteCode, systemIn));
        String result;
        try {
            return future.get(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            result = "Program interrupted.";
        } catch (ExecutionException e) {
            result = e.getCause().getMessage();
        } catch (TimeoutException e) {
            result = "Time Limit Exceeded.";
        } finally {
            future.cancel(true);
        }
        return result != null ? result : "";
    }

    /**
     * @param byteCode
     * @param systemIn
     * @return
     */
    public static String run(byte[] byteCode, String systemIn) {

        // 修改字节码，把 java/lang/System 替换为 com/ywh/olrn/executor/CustomSystem，java/util/Scanner 替换为
        // com/ywh/olrn/executor/CustomScanner
        ClassModifier cm = new ClassModifier(byteCode);
        cm.modifyUTF8Constant("java/lang/System", "com/ywh/olrn/executor/CustomSystem");
        cm.modifyUTF8Constant("java/util/Scanner", "com/ywh/olrn/executor/CustomScanner");

        byte[] modifiedByteCode = cm.getByteCode();

        // 从字节码中加载类
        CustomClassLoader customClassLoader = new CustomClassLoader();

        Class<?> clazz = customClassLoader.loadByte(modifiedByteCode);

        // 设置用户传入的标准输入
        ((CustomInputStream) CustomSystem.in).set(systemIn);
        // 反射执行类的 main 方法，获取执行结果
        try {
            Method mainMethod = clazz.getMethod("main", new Class[]{String[].class});
            mainMethod.invoke(null, new String[]{null});
            String result = CustomSystem.getBufferString();
            CustomSystem.closeBuffer();
            return result;
        } catch (NoSuchMethodException ex) {
            log.error("", ex);
            return null;
        } catch (IllegalAccessException ex) {
            log.error("", ex);
        } catch (InvocationTargetException ex) {
            log.error("", ex);
            ex.getCause().printStackTrace(CustomSystem.err);
        }
        return null;
    }

}
