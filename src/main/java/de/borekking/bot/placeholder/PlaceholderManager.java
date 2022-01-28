package de.borekking.bot.placeholder;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderManager<T> {

    private final List<Placeholder<T>> placeholders;

    private PlaceholderManager(List<Placeholder<T>> placeholders) {
        this.placeholders = placeholders;
    }

    public PlaceholderManager() {
        this(new ArrayList<>());
    }

    @SafeVarargs
    public final String translate(String str, T... t) {
        for (Placeholder<T> ph : this.placeholders)
            str = ph.replace(str, t);

        return str;
    }

    public PlaceholderManager<T> copy() {
        return new PlaceholderManager<>(new ArrayList<>(this.placeholders));
    }

    public void addPlaceholder(Placeholder<T> placeholder) {
        this.placeholders.add(placeholder);
    }
}
