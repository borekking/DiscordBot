package de.borekking.bot.util;

public class Checker {

    public static boolean isEmpty(String str) {
        return isNull(str) || str.trim().isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }
}