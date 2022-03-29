
package com.horizon.base.util;

import java.util.Random;

/**
 * 随机数工具类 <br>
 */
public class RandomUtil {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * 获取范围[0, n]的随机数，
     * 要求n > 0
     */
    public static int random(int n) {
        return RANDOM.nextInt(n + 1);
    }

    /**
     * 获取范围[m, n]的整数，
     * 要求 m < n
     */
    public static int random(int m, int n) {
        int d = n - m;
        return m + RANDOM.nextInt(d + 1);
    }

    /**
     * 获取范围[m, n)的浮点数，
     * 要求 m < n
     */
    public static float random(float m, float n) {
        float d = n - m;
        return m + RANDOM.nextFloat() * d;
    }
}
