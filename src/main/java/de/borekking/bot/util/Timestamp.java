package de.borekking.bot.util;

public enum Timestamp {

    SHORT_DATE_TIME("<t:%d>"),
    LONG_DATE_TIME("<t:%d:F>"),
    SHORT_DATE("<t:%d:d>"),
    LONG_DATE("<t:%d:D>"),
    SHORT_TIME("<t:%d:t>"),
    LONG_TIME("<t:%d:T>"),
    RELATIVE_TIME("<t:%d:R>");

    private final String str;

    Timestamp(String str) {
        this.str = str;
    }

    // Default time
    public String apply() {
        return this.apply(System.currentTimeMillis() / 1000);
    }

    // Specified time
    public String apply(long time) {
        return String.format(this.str, time);
    }

}
