package de.borekking.bot.util;

import java.util.Map;

public class JavaUtils {

    public static String replace(String str, String regex, Object value) {
        if (str == null || regex == null || value == null) return "";

        return str.replaceAll(regex, String.valueOf(value));
    }

    public static String replaceAll(String str, Map<String, Object> map) {
        if (str == null) return "";

        for (String key : map.keySet())
            str = replace(str, key, map.get(key));

        return str;
    }

}
