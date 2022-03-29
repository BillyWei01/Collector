
package com.horizon.collector.config;

import android.app.Application;
import android.util.Log;

import com.horizon.base.config.GlobalConfig;
import com.horizon.doodle.Doodle;


/**
 * APP入口 <br/>
 * 可以在此处进行一些初始化工作 <br/>
 */
public class CollectorApplication extends Application {
    private final static String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();

        // 进行一些必要的初始化
        AppInitManager.initApplication(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (GlobalConfig.DEBUG) {
            Log.i(TAG, "onTrimMemory ... level:" + level);
        }

        Doodle.trimMemory(level);
    }

}
