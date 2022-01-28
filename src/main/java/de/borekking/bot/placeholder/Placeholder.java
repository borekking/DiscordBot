package de.borekking.bot.placeholder;

public abstract class Placeholder<T> {

    private final String key;

    public Placeholder(String key) {
        this.key = key;
    }

    protected abstract String getValue(T... t);

    @SafeVarargs
    public final String replace(String str, T... t) {
        return str.replaceAll(this.key, this.getValue(t));
    }

    protected String getKey() {
        return key;
    }
}
