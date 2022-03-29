
package com.horizon.base.util;

import android.util.Log;



public class LogUtil {
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, Throwable e) {
        Log.e(tag, e.getMessage(), e);
    }
}
