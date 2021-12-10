package de.borekking.bot.util.java;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class Checker {

    public static boolean isEmpty(String str) {
        return isNull(str) || str.trim().isEmpty();
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    // If a test IS true for any element returns false
    // If one or more tests and empty List: returns false
    @SafeVarargs
    public static <T> boolean testList(Collection<T> list, Predicate<T>... tests) {
        for (Predicate<T> test : tests)
            if (list.stream().anyMatch(test))
                return false;
        return true;
    }

    public static <T> boolean listNonNull(Collection<T> list) {
        if (isNull(list)) return false;
        return testList(list, Objects::isNull);
    }
}
