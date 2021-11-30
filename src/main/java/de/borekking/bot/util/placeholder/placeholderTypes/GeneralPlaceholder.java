package de.borekking.bot.util.placeholder.placeholderTypes;

import de.borekking.bot.util.placeholder.Placeholder;

import java.util.function.Supplier;

public class GeneralPlaceholder extends Placeholder<Void> {

    private final Supplier<String> value;

    public GeneralPlaceholder(String key, Supplier<String> value) {
        super(key);
        this.value = value;
    }

    @Override
    protected String getValue(Void... t) {
        if (this.value == null) return this.getKey();
        String v = this.value.get();
        return v == null ? this.getKey() : v;
    }
}
