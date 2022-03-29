

package com.horizon.collector.huaban.source;


public class Pin {
    private static final String BASE_URL = "http://img.hb.aicdn.com/";

    public final String id;
    public final String url;
    public final int width;
    public final int height;

    public Pin(String pinID, String key, int width, int height) {
        this.id = pinID;
        this.url = BASE_URL + key;
        this.width = width;
        this.height = height;
    }
}
