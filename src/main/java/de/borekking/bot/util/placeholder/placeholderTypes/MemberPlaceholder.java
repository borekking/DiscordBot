package de.borekking.bot.util.placeholder.placeholderTypes;

import de.borekking.bot.util.placeholder.Placeholder;
import net.dv8tion.jda.api.entities.Member;

import java.util.function.Function;

public class MemberPlaceholder extends Placeholder<Member> {

    private final Function<Member, String> f;

    public MemberPlaceholder(String key, Function<Member, String> f) {
        super(key);
        this.f = f;
    }

    @Override
    protected String getValue(Member... t) {
        if (t == null || t.length < 1) return this.getKey();
        return this.f == null ? this.getKey() : this.f.apply(t[0]);
    }
}
