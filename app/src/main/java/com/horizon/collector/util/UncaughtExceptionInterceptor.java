

package com.horizon.collector.util;

/**
 * 异常捕获拦截器 <br/>
 * 捕获到异常后，输出错误日志，然后交回给系统的默认异常处理器去处理。
 */

public class UncaughtExceptionInterceptor implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "UncaughtExceptionInterceptor";

    private final static UncaughtExceptionInterceptor INSTANCE = new UncaughtExceptionInterceptor();
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private UncaughtExceptionInterceptor() {
    }

    public static UncaughtExceptionInterceptor getInstance() {
        return INSTANCE;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // log error
        // LogUtil.e(TAG, ex);
        if (mDefaultHandler != null && mDefaultHandler != this) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }
}

