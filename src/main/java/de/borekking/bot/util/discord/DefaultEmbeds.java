package de.borekking.bot.util.discord;

import java.awt.*;
import java.time.OffsetDateTime;

public enum DefaultEmbeds {

    JOIN_EMBED(new MyEmbedBuilder().description("Welcome to **%servername%**, %user%!%nextLine%You are the %memberCount%. member!").title("Welcome!").timestamp(OffsetDateTime.now())),
    LEAVE_EMBED(new MyEmbedBuilder().description("Bye, bye, %user%!%nextLine%%nextLine%Left members: %memberCount%").title("Bye!").timestamp(OffsetDateTime.now())),
    EXIT_EMBED(new MyEmbedBuilder().title("Disabled bot")),
    RELOAD_EMBED(new MyEmbedBuilder().color(Color.BLUE).title("Reload").description("Reloaded bot").timestamp(OffsetDateTime.now()));

    private final MyEmbedBuilder embed;

    DefaultEmbeds(MyEmbedBuilder builder) {
        this.embed = builder;
    }

    public MyEmbedBuilder getEmbed() {
        return embed;
    }
}
