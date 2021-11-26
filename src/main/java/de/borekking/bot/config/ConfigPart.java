package de.borekking.bot.config;

import java.util.function.Function;

public class ConfigPart {

    private String key;
    private Object value;

    public ConfigPart(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public  <T> T getValue(Function<Object, T> f) {
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
