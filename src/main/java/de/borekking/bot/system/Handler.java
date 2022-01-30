package de.borekking.bot.system;

import net.dv8tion.jda.api.entities.User;

public interface Handler {

    boolean use(InformationProvider provider);

    User undo(String userID, String s);
}
