package de.borekking.bot.config;

import java.util.function.Function;

public enum ConfigSetting {

    TOKEN("token", ""),
    GUILD_ID("guildID", "");

    private String key;
    private Object value;

    ConfigSetting(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    private <T> T wrapper(Function<Object, T> f) {
        return f.apply(this.value);
    }

    public String getValueAsString() {
        return this.wrapper(String::valueOf);
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
