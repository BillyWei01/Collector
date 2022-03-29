package com.horizon.collector.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.horizon.base.config.GlobalConfig;
import com.horizon.base.util.LogUtil;
import com.horizon.collector.common.http.HttpClient;
import com.horizon.collector.setting.model.UserSetting;
import com.horizon.collector.util.UncaughtExceptionInterceptor;
import com.horizon.doodle.Doodle;
import com.horizon.doodle.interfaces.GifDecoder;


import org.jetbrains.annotations.NotNull;

import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

class AppInitManager {
    static void initApplication(Application context) {
        if (AppConfig.APPLICATION_ID.equals(getProcessName(context))) {
            // 先初始化AppContext
            GlobalConfig.setAppContext(context);

            UncaughtExceptionInterceptor.getInstance().init();

            com.horizon.doodle.worker.LogProxy.INSTANCE.init(GlobalLogger.getInstance());

            Doodle.config()
                    .setUserAgent(HttpClient.USER_AGENT)
                    .setGifDecoder(new GifDecoder() {
                        @NotNull
                        @Override
                        public Drawable decode(@NotNull byte[] bytes) throws Exception {
                            return new GifDrawable(bytes);
                        }
                    });
        }
    }

    private static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps != null) {
                final int curPid = android.os.Process.myPid();
                for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
                    if (runningApp.pid == curPid) {
                        return runningApp.processName;
                    }
                }
            }
        } else {
            LogUtil.e("AppInitManager", "Get ActivityManager service failed.");
        }
        return AppConfig.APPLICATION_ID;
    }

}
