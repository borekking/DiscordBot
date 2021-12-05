package de.borekking.bot.util.placeholder.placeholderTypes;

import de.borekking.bot.util.placeholder.Placeholder;
import net.dv8tion.jda.api.entities.User;

import java.util.function.Function;

public class UserPlaceholder extends Placeholder<User> {

    private final Function<User, String> f;

    public UserPlaceholder(String key, Function<User, String> f) {
        super(key);
        this.f = f;
    }

    @Override
    protected String getValue(User... t) {
        if (t == null || t.length < 1) return this.getKey();
        return this.f == null ? this.getKey() : this.f.apply(t[0]);
    }
}
