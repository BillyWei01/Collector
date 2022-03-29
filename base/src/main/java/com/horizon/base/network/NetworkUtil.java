
package com.horizon.base.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.horizon.base.config.GlobalConfig;

public class NetworkUtil {

    /**
     * 网络是否连通
     * @return
     */
    public static boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                GlobalConfig.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * wifi是否连通
     * @return
     */
    public static boolean isWifiConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                GlobalConfig.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected()
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }
}
