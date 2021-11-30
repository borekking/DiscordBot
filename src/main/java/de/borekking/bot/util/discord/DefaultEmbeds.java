package de.borekking.bot.util.discord;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;

public enum DefaultEmbeds {

    JOIN_EMBED(new MyEmbedBuilder().description("Welcome to **%servername%**, %user%!\nYou are the %memberCount%. member!").title("Welcome!").timestamp(OffsetDateTime.now())),
    EXIT_EMBED(new MyEmbedBuilder().title("Disabled bot"));

    private final MessageEmbed embed;

    DefaultEmbeds(MyEmbedBuilder builder) {
        this.embed = builder.build();
    }

    public MessageEmbed getEmbed() {
        return embed;
    }
}
