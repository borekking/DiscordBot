package de.borekking.bot.config;

import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigPart {

    private final String key;
    private Object value;

    public ConfigPart(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public <T> ConfigPart(String key, Supplier<T> value) {
        this.key = key;
        this.value = value.get();
    }

    public  <T> T getValue(Function<Object, T> f) {
        if (this.value == null) return null;
        return f.apply(this.value);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
