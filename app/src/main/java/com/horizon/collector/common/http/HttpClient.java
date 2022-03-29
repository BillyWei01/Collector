package com.horizon.collector.common.http;

import com.horizon.base.config.PathManager;
import com.horizon.base.util.LogUtil;
import com.horizon.doodle.Utils;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClient {
    private static final String TAG = "HttpClient";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1";

    private static final OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .cache(new Cache(new File(PathManager.CACHE_PATH, "http"), 128 << 20L))
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header("User-Agent", USER_AGENT).build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public static String request(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.string();
            }
        }
        return "";
    }
}
