package de.borekking.bot.util.placeholder.placeholderTypes;

import de.borekking.bot.util.placeholder.Placeholder;

public class GeneralPlaceholder extends Placeholder<Void> {

    private final String value;

    public GeneralPlaceholder(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    protected String getValue(Void... t) {
        return this.value == null ? this.getKey() : this.value;
    }
}
