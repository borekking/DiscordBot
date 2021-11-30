package de.borekking.bot.util.placeholder;

import net.dv8tion.jda.api.entities.Member;

public interface Replaceable {

    void replace(PlaceholderTranslator translator);

    void replace(PlaceholderTranslator translator, Member member);

}
