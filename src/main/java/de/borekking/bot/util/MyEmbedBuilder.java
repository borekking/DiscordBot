package de.borekking.bot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyEmbedBuilder implements Replaceable {

    /*
     * More advanced EmbedBuilder
     *
     */

    // fields
    private final List<MessageEmbed.Field> fields;
    // description
    private String description;
    // color
    private int color;
    // title
    private String title, url;
    // timestamp
    private OffsetDateTime timestamp;
    // author
    private MessageEmbed.AuthorInfo author;
    // footer
    private MessageEmbed.Footer footer;
    // image
    private MessageEmbed.ImageInfo image;
    // thumbnail
    private MessageEmbed.Thumbnail thumbnail;

    // Embed
    private boolean change;
    private MessageEmbed embed;

    public MyEmbedBuilder() {
        this.fields = new ArrayList<>();
    }

    public MyEmbedBuilder(MessageEmbed embed) {
        this.fields = embed.getFields();
        this.description = embed.getDescription();
        this.color = embed.getColorRaw();
        this.title = embed.getTitle();
        this.url = embed.getUrl();
        this.timestamp = embed.getTimestamp();
        this.author = embed.getAuthor();
        this.footer = embed.getFooter();
        this.image = embed.getImage();
    }

    public MyEmbedBuilder field(String name, String value, boolean inline) {
        this.fields.add(new MessageEmbed.Field(name, value, inline));
        return this.changed();
    }

    public MyEmbedBuilder fields(Map<String, String> fields, boolean inline) {
        fields.forEach((name, value) -> this.field(name, value, inline));
        return this.changed();
    }

    public MyEmbedBuilder description(String description) {
        this.description = description;
        return this.changed();
    }

    public MyEmbedBuilder appendDescription(String description) {
        if (description != null)
            this.description += description;
        return this.changed();
    }

    public MyEmbedBuilder color(Color color) {
        this.color = color.getRGB();
        return this.changed();
    }

    public MyEmbedBuilder title(String title) {
        this.title = title;
        return this.changed();
    }

    public MyEmbedBuilder url(String url) {
        this.url = url;
        return this.changed();
    }

    public MyEmbedBuilder timestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this.changed();
    }

    public MyEmbedBuilder thumbnail(String url) {
        this.thumbnail = new MessageEmbed.Thumbnail(url, null, 0, 0);
        return this.changed();
    }

    public MyEmbedBuilder author(String name) {
        return this.author(name, null, null);
    }

    public MyEmbedBuilder author(String name, String url) {
        return this.author(name, url, null);
    }

    public MyEmbedBuilder author(String name, String url, String iconUrl) {
        this.author = new MessageEmbed.AuthorInfo(name, url, iconUrl, null);
        return this.changed();
    }

    public MyEmbedBuilder footer(String text) {
        return this.footer(text, null);
    }

    public MyEmbedBuilder footer(String text, String iconUrl) {
        this.footer = new MessageEmbed.Footer(text, iconUrl, null);
        return this.changed();
    }

    public MyEmbedBuilder image(String image) {
        this.image = new MessageEmbed.ImageInfo(image, null, 0, 0);
        return this.changed();
    }

    public MyEmbedBuilder addBlankField(boolean inline) {
        return this.field("\u200e", "\u200e", inline);
    }

    public MyEmbedBuilder clearFields() {
        this.fields.clear();
        return this.changed();
    }

    @Override
    public void replace(String regex, Object value) {
        // Fields (name, value)
        List<MessageEmbed.Field> fields1 = new ArrayList<>();
        this.fields.forEach(field ->
            fields1.add(new MessageEmbed.Field(replace(field.getName(), regex, value), replace(field.getValue(), regex, value), field.isInline()))
        );
        this.fields.clear();
        this.fields.addAll(fields1);

        // Description
        this.description = replace(this.description, regex, value);

        // Title
        this.title = replace(this.title, regex, value);

        // Author (name)
        if (this.author != null)
            this.author = new MessageEmbed.AuthorInfo(replace(this.author.getName(), regex, value), this.author.getUrl(), this.author.getIconUrl(), null);

        // Footer (text)
        if (this.footer != null)
            this.footer = new MessageEmbed.Footer(replace(this.footer.getText(), regex, value), this.footer.getIconUrl(), null);
    }

    @Override
    public void replace(Map<String, Object> map) {
        // Fields (name, value)
        List<MessageEmbed.Field> fields1 = new ArrayList<>();
        this.fields.forEach(field ->
                fields1.add(new MessageEmbed.Field(replaceAll(field.getName(), map), replaceAll(field.getValue(), map), field.isInline()))
        );
        this.fields.clear();
        this.fields.addAll(fields1);

        // Description
        this.description = replaceAll(this.description, map);

        // Title
        this.title = replaceAll(this.title, map);

        // Author (name)
        if (this.author != null)
            this.author = new MessageEmbed.AuthorInfo(replaceAll(this.author.getName(), map), this.author.getUrl(), this.author.getIconUrl(), null);

        // Footer (text)
        if (this.footer != null)
            this.footer = new MessageEmbed.Footer(replaceAll(this.footer.getText(), map), this.footer.getIconUrl(), null);
    }

    public MessageEmbed build() {
        if (!this.change) return this.embed;
        if (this.isEmpty()) return null;

        EmbedBuilder builder = new EmbedBuilder();

        // fields
        this.fields.forEach(builder::addField);

        // description
        builder.setDescription(this.description);

        // color
        builder.setColor(this.color);

        // title
        if (this.title == null && this.url != null)
            builder.setTitle("", this.url);
        else
            builder.setTitle(this.title, this.url);

        // timestamp
        builder.setTimestamp(this.timestamp);

        // author
        if (this.author != null)
            builder.setAuthor(this.author.getName(), this.author.getUrl(), this.author.getIconUrl());

        // footer
        if (this.footer != null)
            builder.setFooter(this.footer.getText(), this.footer.getIconUrl());

        // image
        if (this.image != null)
            builder.setImage(this.image.getUrl());

        // thumbnail
        if (this.thumbnail != null)
            builder.setImage(this.thumbnail.getUrl());

        return this.embed = builder.build();
    }

    private MyEmbedBuilder changed() {
        if (!this.change)
            this.change = true;
        return this;
    }

    private String replace(String str, String regex, Object value) {
        if (str == null || regex == null || value == null) return "";

        return str.replaceAll(regex, String.valueOf(value));
    }

    private String replaceAll(String str, Map<String, Object> map) {
        if (str == null) return "";

        for (String key : map.keySet())
            str = this.replace(str, key, map.get(key));
        return str;
    }

    private boolean isEmpty() {
        return (this.title == null || this.title.trim().isEmpty()) && this.timestamp == null && this.thumbnail == null && this.author == null && this.footer == null && this.image == null && this.color == 536870911 && this.description.length() == 0 && this.fields.isEmpty();
    }
}
