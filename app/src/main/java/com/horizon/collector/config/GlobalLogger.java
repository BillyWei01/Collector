package com.horizon.collector.config;

import com.horizon.base.util.LogUtil;
import com.horizon.collector.BuildConfig;

import org.jetbrains.annotations.NotNull;

public class GlobalLogger implements com.horizon.doodle.worker.ILogger, io.fastkv.FastKV.Logger {
    private static final  GlobalLogger INSTANCE = new GlobalLogger();
    private GlobalLogger(){
    }

    public static GlobalLogger getInstance(){
        return INSTANCE;
    }
    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void e(@NotNull String tag, @NotNull Throwable e) {
        LogUtil.e(tag, e);
    }

    @Override
    public void i(String name, String message) {
        android.util.Log.i(name, message);
    }

    @Override
    public void w(String name, Exception e) {
        LogUtil.e(name, e);
    }

    @Override
    public void e(String name, Exception e) {
        LogUtil.e(name, e);
    }
}
