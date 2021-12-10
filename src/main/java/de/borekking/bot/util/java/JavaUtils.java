package de.borekking.bot.util.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JavaUtils {

    private JavaUtils() {
    }

    public static <T> List<T> getAsList(T[] arr) {
        if (arr == null || arr.length == 0) return new ArrayList<>();
        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public static <T> List<T> copy(List<T> list) {
        return new ArrayList<>(list);
    }

    public static <T> void addAllIf(List<T> target, List<T> scr, Predicate<T> test) {
        for (T t : scr)
            if (test.test(t))
                target.add(t);
    }
}
