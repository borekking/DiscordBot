package de.borekking.bot.util.discord.embed;

import de.borekking.bot.placeholder.PlaceholderTranslator;
import de.borekking.bot.placeholder.Replaceable;
import de.borekking.bot.util.java.Checker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    private String title, titleUrl;
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

    public MyEmbedBuilder(EmbedType type) {
        this();

        switch (type) {
            case ERROR:
                this.title("Error");
                this.color(Color.RED);
                break;
            case SUCCESS:
                this.title("Success");
                this.color(Color.GREEN);
                break;
            case NEUTRAL:
                this.color(Color.BLUE);
                break;
        }

        this.changed();
    }

    public MyEmbedBuilder(MessageEmbed embed) {
        this.fields = new ArrayList<>(embed.getFields());
        this.description = embed.getDescription();
        this.color = embed.getColorRaw();
        this.title = embed.getTitle();
        this.titleUrl = embed.getUrl();
        this.timestamp = embed.getTimestamp();
        this.author = embed.getAuthor();
        this.footer = embed.getFooter();
        this.image = embed.getImage();

        this.changed();
    }

    public MyEmbedBuilder copy() {
        return new MyEmbedBuilder(this.build());
    }

    public MyEmbedBuilder field(String name, String value, boolean inline) {
        if (Checker.isEmpty(name) && Checker.isEmpty(value)) return this;
        this.fields.add(new MessageEmbed.Field(name, value, inline));
        return this.changed();
    }

    public MyEmbedBuilder fields(Map<String, String> fields, boolean inline) {
        fields.forEach((name, value) -> this.field(name, value, inline));
        return this.changed();
    }

    public MyEmbedBuilder description(String description) {
        return this.apply(s -> this.description = s, s -> !Checker.isEmpty(s) && !s.equals(this.description), description, false);
    }

    public MyEmbedBuilder appendDescription(String description) {
        return this.apply(s -> this.description += s, s -> !Checker.isEmpty(s), description, false);
    }

    public MyEmbedBuilder color(Color color) {
        return this.apply(c -> this.color = c, c -> this.color != c, color.getRGB());
    }

    public MyEmbedBuilder title(String title, String url) {
        if (Checker.isEmpty(title)) return this;
        this.title = title;
        if (!Checker.isEmpty(url))
            this.titleUrl = url;
        return this.changed();
    }

    public MyEmbedBuilder title(String title) {
        return this.title(title, null);
    }

    public MyEmbedBuilder timestamp(OffsetDateTime timestamp) {
        return this.apply(t -> this.timestamp = t, t -> this.timestamp == null || !this.timestamp.equals(t), timestamp);
    }

    public MyEmbedBuilder thumbnail(String url) {
        return this.apply(u -> this.thumbnail = new MessageEmbed.Thumbnail(u, null, 0, 0), u -> this.thumbnail == null || !u.equals(this.thumbnail.getUrl()), url);
    }

    public MyEmbedBuilder author(String name, String url, String iconUrl) {
        return this.apply(s -> this.author = new MessageEmbed.AuthorInfo(s, url, iconUrl, null), s -> !Checker.isEmpty(s), name, false);
    }

    public MyEmbedBuilder author(String name, String url) {
        return this.author(name, url, null);
    }

    public MyEmbedBuilder author(String name) {
        return this.author(name, null, null);
    }

    public MyEmbedBuilder footer(String text, String iconUrl) {
        return this.apply(s -> this.footer = new MessageEmbed.Footer(s, iconUrl, null), s -> !Checker.isEmpty(s), text, false);
    }

    public MyEmbedBuilder footer(String text) {
        return this.footer(text, null);
    }

    public MyEmbedBuilder image(String url) {
        return this.apply(u -> this.image = new MessageEmbed.ImageInfo(u, null, 0, 0), u -> !Checker.isEmpty(u), url, false);
    }

    public MyEmbedBuilder addBlankField(boolean inline) {
        return this.field("\u200e", "\u200e", inline);
    }

    public MyEmbedBuilder clearFields() {
        return apply(this.fields::clear, !this.fields.isEmpty());
    }

    @Override
    public void replace(PlaceholderTranslator translator) {
        // Fields (name, value)
        List<MessageEmbed.Field> fields1 = new ArrayList<>();
        this.fields.forEach(field ->
                fields1.add(new MessageEmbed.Field(translator.translate(field.getName()), translator.translate(field.getValue()), field.isInline()))
        );
        this.fields.clear();
        this.fields.addAll(fields1);

        // Description
        this.description = translator.translate(this.description);

        // Title
        this.title = translator.translate(this.title);

        // Author (name)
        if (this.author != null)
            this.author = new MessageEmbed.AuthorInfo(translator.translate(this.author.getName()), this.author.getUrl(), this.author.getIconUrl(), null);

        // Footer (text)
        if (this.footer != null)
            this.footer = new MessageEmbed.Footer(translator.translate(this.footer.getText()), this.footer.getIconUrl(), null);
    }

    @Override
    public void replace(PlaceholderTranslator translator, User user) {
        // Fields (name, value)
        List<MessageEmbed.Field> fields1 = new ArrayList<>();
        this.fields.forEach(field ->
                    fields1.add(new MessageEmbed.Field(translator.translate(field.getName(), user), translator.translate(field.getValue(), user), field.isInline()))
        );
        this.fields.clear();
        this.fields.addAll(fields1);

        // Description
        this.description = translator.translate(this.description, user);

        // Title
        this.title = translator.translate(this.title, user);

        // Author (name)
        if (this.author != null)
            this.author = new MessageEmbed.AuthorInfo(translator.translate(this.author.getName(), user), this.author.getUrl(), this.author.getIconUrl(), null);

        // Footer (text)
        if (this.footer != null)
            this.footer = new MessageEmbed.Footer(translator.translate(this.footer.getText(), user), this.footer.getIconUrl(), null);
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
        if (this.title == null || this.title.isEmpty() && this.titleUrl != null)
            builder.setTitle(" ", this.titleUrl);
        else
            builder.setTitle(this.title, this.titleUrl);

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
            builder.setThumbnail(this.thumbnail.getUrl());

        this.change = false;
        return this.embed = builder.build();
    }

    private MyEmbedBuilder changed() {
        if (!this.change)
            this.change = true;
        return this;
    }

    private MyEmbedBuilder apply(Runnable r, boolean condition) {
        if (condition) {
            r.run();
            return this.changed();
        }

        return this;
    }

    private <T> MyEmbedBuilder apply(Consumer<T> c, Predicate<T> condition, T t, boolean nullCheck) {
        if ((!nullCheck || !Checker.isNull(t)) && condition.test(t)) {
            c.accept(t);
            return this.changed();
        }

        return this;
    }

    private <T> MyEmbedBuilder apply(Consumer<T> c, Predicate<T> p, T t) {
        return this.apply(c, p, t, true);
    }

    private <T> MyEmbedBuilder apply(Consumer<T> c, T t) {
        return this.apply(c, o -> true, t);
    }

    public boolean isEmpty() {
        return (this.title == null || this.title.trim().isEmpty()) && this.timestamp == null && this.thumbnail == null && this.author == null && this.footer == null && this.image == null && this.color == 536870911 && this.description.length() == 0 && this.fields.isEmpty();
    }
}
