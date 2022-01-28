package de.borekking.bot.placeholder;

import net.dv8tion.jda.api.entities.User;

public interface Replaceable {

    void replace(PlaceholderTranslator translator);

    void replace(PlaceholderTranslator translator, User user);

}
