

package com.horizon.base.config;

import android.app.Application;
import android.content.Context;

import com.horizon.base.BuildConfig;

/**
 * 全局的编译配置 <br/>
 */

public class GlobalConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG_FLAG;

    private static Context sAppContext;

    /**
     * 需在App启动的第一时间给 sAppContext 赋值，
     * 否则依赖 getAppContext() 的地方会有问题。
     *
     * @param context Application
     */
    public static void setAppContext(Application context){
        sAppContext = context;
    }

    public static Context getAppContext(){
        return sAppContext;
    }


}
