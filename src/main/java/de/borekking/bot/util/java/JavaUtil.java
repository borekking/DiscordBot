package de.borekking.bot.util.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaUtil {

    public static <T> List<T> getAsList(T[] arr) {
        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public static <T> List<T> copy(List<T> arr) {
        return new ArrayList<>(arr);
    }
}
