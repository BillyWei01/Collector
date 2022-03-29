
package com.horizon.base.util;

import androidx.annotation.StringRes;

import com.horizon.base.config.GlobalConfig;


public class ResUtil {

    public static String getStr(@StringRes int resId) {
        return GlobalConfig.getAppContext().getString(resId);
    }
    public static String getStr(@StringRes int resId, Object... formatArgs){
        return GlobalConfig.getAppContext().getString(resId, formatArgs);
    }

}
