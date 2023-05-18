package com.example.chat.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xingboyuan
 * @date 2023/5/9 14:52
 */
public class CacheHelper {


    private static ThreadLocal<Map<String, String>> cache = new ThreadLocal<>();

    public static void put(String key, String value) {
        Map<String, String> map = cache.get();
        if (map == null) {
            map = new HashMap<>();
            cache.set(map);
        }
        map.put(key, value);
    }

    public static String get(String key) {
        Map<String, String> map = cache.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

}
