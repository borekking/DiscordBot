package de.borekking.bot.time;

import java.util.Map;
import java.util.StringJoiner;

public class DurationUtils {

    public static long getValue(String str) throws TimeEnum.IllegalDurationException {
        return getValue(TimeEnum.getValueOf(str));
    }

    public static long getValue(Map<Integer, TimeEnum> map) {
        long sum = 0L;
        for (int key : map.keySet())
            sum += map.get(key).getMillis() * key;
        return sum;
    }

    public static String getMessage(Map<Integer, TimeEnum> map) {
        return getMessage(getValue(map));
    }

    public static String getMessage(long time) {
        if (time <= 0) return TimeEnum.PERMANENT.getName();

        long rest = time;
        StringJoiner msg = new StringJoiner(", ");

        for (TimeEnum timeEnum : TimeEnum.getSortedValues()) {
            long millis = timeEnum.getMillis();
            if (millis < 0) continue;

            int times = 0;
            while (rest > millis) {
                rest = rest - millis;
                times++;
            }

            if (times > 0) msg.add(times + " " + timeEnum.getName() + "(s)");
        }
        return msg.toString();
    }
}
