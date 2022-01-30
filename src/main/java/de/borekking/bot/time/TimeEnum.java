package de.borekking.bot.time;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum TimeEnum {

    SECOND("s", 1000L),
    MINUTE("min", SECOND.millis * 60L),
    HOUR("h", MINUTE.millis * 60L),
    DAY("d", HOUR.millis * 24L),
    WEEK("w", DAY.millis * 7L),
    MONTH("m", DAY.millis * 30L),
    YEAR("y", DAY.millis * 365L),
    PERMANENT("p", -1L);

    public static class IllegalDurationException extends Exception {
        public IllegalDurationException(String name) {
            super("\"" + name + "\" is not a valid time short name!");
        }
    }

    private static List<TimeEnum> sortedValues;

    private final String shortName, name;
    private final long millis;

    TimeEnum(String shortName, long millis) {
        this.shortName = shortName;
        this.name = this.name().toLowerCase();
        this.millis = millis;
    }

    public static List<TimeEnum> getSortedValues() {
        if (sortedValues == null) {
            sortedValues = Arrays.stream(values())
                    .sorted((x, y) -> Long.compare(y.millis, x.millis)) // Sort reversed.
                    .collect(Collectors.toList());
        }
        return sortedValues;
    }

    // Methode to get a Strings "time"-value as TimeEnums
    static Map<Integer, TimeEnum> getValueOf(String str) throws IllegalDurationException {
        // If String with spaces is provided, split it on spaces and use this methode to get each one's value
        if (str.contains(" ")) {
            Map<Integer, TimeEnum> map = new HashMap<>();

             for (String s : str.split(" "))
                 map.putAll(getValueOf(s));

            return map;
        }

        // Find the first Letter's index in str.
        int len = str.length(), index = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isLetter(str.charAt(i))) {
                index = i;
                break;
            }
        }

        // Get the amount (integer) and TimeEnum (string) parts of str.
        String amount = str.substring(0, index), name = str.substring(index);
        // Get the - to the given time-(short)name belonging - TimeEnum
        TimeEnum time = getByShortName(name);
        if (time == null) throw new IllegalDurationException(name);

        // Get times of TimeEnum
        int times = 1;
        try {
            times = Integer.parseInt(amount);
        } catch (NumberFormatException ignored) {
        }

        // Return a single element map with times and time.
        Map<Integer, TimeEnum> map = new HashMap<>();
        map.put(times, time);
        return map;
    }

    private static TimeEnum getByShortName(String shortName) {
        for (TimeEnum time : TimeEnum.values())
            if (time.getShortName().equalsIgnoreCase(shortName))
                return time;
        return null;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public long getMillis() {
        return millis;
    }
}
