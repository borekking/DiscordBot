package de.borekking.bot.util.placeholder;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderManager<T> {

    private final List<Placeholder<T>> placeholders;

    public PlaceholderManager() {
        this.placeholders = new ArrayList<>();
    }

    @SafeVarargs
    public final String translate(String str, T... t) {
        for (Placeholder<T> ph : this.placeholders)
            str = ph.replace(str, t);

        return str;
    }

    public void addPlaceholder(Placeholder<T> placeholder) {
        this.placeholders.add(placeholder);
    }
}
