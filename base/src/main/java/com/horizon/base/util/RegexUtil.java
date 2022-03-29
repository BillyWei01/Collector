
package com.horizon.base.util;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类 <br>
 * 封装一些常用的字符解析方法
 */
public class RegexUtil {

    /**
     * 获取匹配正则的所有分组
     */
    public static List<String> getGroups(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        List<String> groupList = new ArrayList<>();
        while (m.find()) {
            String group = m.group();
            if (!TextUtils.isEmpty(group)) {
                groupList.add(m.group());
            }
        }
        return groupList;
    }

    /**
     * 获取匹配正则的第一个分组
     */
    public static String getGroup(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(text);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * 提取字符串<br>
     * 获取有特定前缀和后缀的字符串
     *
     * @param prefix       字符串前缀
     * @param suffix       字符串后缀
     * @param contentRegex 字符串本身的正则
     * @param text         源字符串
     * @return 符合条件的字符串片段
     */
    public static String extractString(String prefix, String suffix, String contentRegex, String text) {
        StringBuilder regexBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(prefix)) {
            regexBuilder.append("(?<=").append(prefix).append(')');
        }
        regexBuilder.append(contentRegex);
        if (!TextUtils.isEmpty(suffix)) {
            regexBuilder.append("(?=").append(suffix).append(')');
        }
        return getGroup(regexBuilder.toString(), text);
    }

    public static String extractString(String prefix, String suffix, String text) {
        return extractString(prefix, suffix, ".+?", text);
    }

}