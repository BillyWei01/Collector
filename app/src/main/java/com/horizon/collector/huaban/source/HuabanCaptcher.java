
package com.horizon.collector.huaban.source;


import android.text.TextUtils;

import com.horizon.base.util.RegexUtil;
import com.horizon.collector.common.http.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HuabanCaptcher {
    private static final String BASE_URL = "https://huaban.com/favorite/";

    public static List<Pin> pickPins(String id, String pinID) throws IOException {
        List<Pin> pinList = new ArrayList<>();
        String url = BASE_URL + id;
        if (!TextUtils.isEmpty(pinID)) {
            url += "?i5p998kw&max=" + pinID + "&limit=20&wfl=1";
        }
        List<String> pinIdList = extractPins(url);
        for (String pinStr : pinIdList) {
            Pin pin = parsePin(pinStr);
            if (pin != null) {
                pinList.add(pin);
            }
        }
        return pinList;
    }

    private static List<String> extractPins(String url) throws IOException {
        String content = HttpClient.request(url);
        int start = content.indexOf("app.page[\"pins\"]");
        int end = content.indexOf("app.page", start + 1000);
        String pinPages = content.substring(start, end);
        String regex = "(\"pin_id\":)[^\\}]+?(?=, \"frames)";
        return RegexUtil.getGroups(regex, pinPages);
    }

    private static Pin parsePin(String str) {
        if (!TextUtils.isEmpty(str)) {
            String pinID = RegexUtil.getGroup("\\d+", str);
            str = str.substring(str.indexOf("key\":\""));
            String key = RegexUtil.extractString("key\":\"", "\"", str);
            String type = RegexUtil.extractString("image/", "\",", str);
            String width = RegexUtil.extractString("width(\\D{2,3})", null, "\\d+", str);
            String height = RegexUtil.extractString("height(\\D{2,3})", null, "\\d+", str);
            if (acceptType(type)
                    && !TextUtils.isEmpty(pinID)
                    && !TextUtils.isEmpty(key)
                    && !TextUtils.isEmpty(width)
                    && !TextUtils.isEmpty(height)) {
                return new Pin(pinID, key, Integer.parseInt(width), Integer.parseInt(height));
            }
        }
        return null;
    }

    private static boolean acceptType(String type) {
        return "jpeg".equals(type) || "png".equals(type);
    }

}
