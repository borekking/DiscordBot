package de.borekking.bot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Map;

public class MyEmbedBuilder {

    /*
     * Has:
     * - fields: one, map
     * - description: set, append
     * - color
     * - title: title, url
     * - timestamp
     * - thumbnail
     * - author: name, url, iconUrl (String x3)
     * - footer: text, iconUrl
     * - image
     *
     */

    private final EmbedBuilder embed;

    public MyEmbedBuilder() {
        this.embed = new EmbedBuilder();
    }

    public MyEmbedBuilder field(String name, String value, boolean inline) {
        this.embed.addField(name, value, inline);
        return this;
    }

    public MyEmbedBuilder fields(Map<String, String> fields, boolean inline) {
        fields.forEach((name, value) -> this.field(name, value, inline));
        return this;
    }

    public MyEmbedBuilder description(String description) {
        this.embed.setDescription(description);
        return this;
    }

    public MyEmbedBuilder appendDescription(String description) {
        this.embed.appendDescription(description);
        return this;
    }

    public MyEmbedBuilder color(Color color) {
        this.embed.setColor(color);
        return this;
    }

    public MyEmbedBuilder title(String title) {
        this.embed.setTitle(title);
        return this;
    }

    public MyEmbedBuilder title(String title, String url) {
        this.embed.setTitle(title, url);
        return this;
    }

    public MyEmbedBuilder timestamp(OffsetDateTime timestamp) {
        this.embed.setTimestamp(timestamp);
        return this;
    }

    public MyEmbedBuilder thumbnail(String url) {
        this.embed.setThumbnail(url);
        return this;
    }

    public MyEmbedBuilder author(String author) {
        this.embed.setAuthor(author);
        return this;
    }

    public MyEmbedBuilder author(String name, String url) {
        this.embed.setAuthor(name, url);
        return this;
    }

    public MyEmbedBuilder author(String name, String url, String iconUrl) {
        this.embed.setAuthor(name, url, iconUrl);
        return this;
    }

    public MyEmbedBuilder footer(String footer) {
        this.embed.setFooter(footer);
        return this;
    }

    public MyEmbedBuilder footer(String text, String iconUrl) {
        this.embed.setFooter(text, iconUrl);
        return this;
    }

    public MyEmbedBuilder image(String image) {
        this.embed.setImage(image);
        return this;
    }

    public MyEmbedBuilder addBlankField(boolean inline) {
        this.embed.addBlankField(inline);
        return this;
    }

    public MyEmbedBuilder clearFields() {
        this.embed.clearFields();
        return this;
    }

    public MessageEmbed build() {
        return this.embed.build();
    }
}
