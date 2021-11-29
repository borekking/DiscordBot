package de.borekking.bot.util;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Color;
import java.time.OffsetDateTime;

public class JSONEmbedUtil {

    /*
     * JSONObject structure
     *
     * "fields": JSONArray of JSONObjects with "name":String, "value":String, "inline":boolean
     * "description": description:String
     * "color":int
     * "title":String
     * "url":String
     * "timestamp":OffsetDateTime (?)
     * "author": JSONObjects with "name":String, "url":String, "iconUrl":String
     * "footer": JSONObjects with "text":String, "iconUrl":String
     * "imageURL":String
     *
     */

    // static Methode to get MyEmbedBuilder from JSONObject
    public static MyEmbedBuilder toMyEmbedBuilder(JSONObject object) {
        MyEmbedBuilder builder = new MyEmbedBuilder();

        // Fields
        JSONArray fieldsArray = (JSONArray) object.get("fields");

        for (Object e : fieldsArray) {
            JSONObject fieldObject = (JSONObject) e;

            String name = (String) fieldObject.get("name");
            String value = (String) fieldObject.get("value");
            boolean inline = (boolean) fieldObject.get("inline");

            builder.field(name, value, inline);
        }

        // Description
        String description = (String) object.get("description");
        builder.description(description);

        // Color
        long color = (long) object.get("color");
        builder.color(new Color((int) color));

        // Title (URL)
        String title = (String) object.get("title");
        String url = (String) object.get("url");
        if (url != null)
            builder.url(url);
        else
            builder.title(title);

        // Timestamp
        OffsetDateTime timestamp = (OffsetDateTime) object.get("timestamp");
        builder.timestamp(timestamp);

        // Author
        JSONObject authorObject = (JSONObject) object.get("author");
        String authorName = (String) authorObject.get("name");
        String authorURL = (String) authorObject.get("url");
        String authorIconUrl = (String) authorObject.get("iconUrl");
        builder.author(authorName, authorURL, authorIconUrl);

        // Footer
        JSONObject footerObject = (JSONObject) object.get("footer");
        String footerText = (String) footerObject.get("text");
        String footerIconUrl = (String) footerObject.get("iconUrl");
        builder.footer(footerText, footerIconUrl);

        // Image
        String imageURL = (String) object.get("imageURL");
        builder.image(imageURL);

        return builder;
    }

    // to JSONObject
    public static JSONObject toJSONObject(MessageEmbed embed) {
        JSONObject object = new JSONObject();

        // Fields
        JSONArray fieldsArray = new JSONArray();

        for (MessageEmbed.Field field : embed.getFields()) {
            JSONObject fieldObject = new JSONObject();
            fieldObject.put("name", field.getName());
            fieldObject.put("value", field.getValue());
            fieldObject.put("inline", field.isInline());
            fieldsArray.add(fieldObject);
        }

        object.put("fields", fieldsArray);

        // Description
        object.put("description", embed.getDescription());

        // Color
        object.put("color", embed.getColorRaw());

        // Title
        object.put("title", embed.getTitle());

        // URL
        object.put("url", embed.getUrl());

        // Timestamp
        object.put("timestamp", embed.getTimestamp());

        // Author
        MessageEmbed.AuthorInfo author = embed.getAuthor();

        JSONObject authorObject = new JSONObject();
        authorObject.put("name", (author != null) ? author.getName() : null);
        authorObject.put("url", (author != null) ? author.getUrl() : null);
        authorObject.put("iconUrl", (author != null) ? author.getIconUrl() : null);

        object.put("author", authorObject);

        // Footer
        MessageEmbed.Footer footer = embed.getFooter();

        JSONObject footerObject = new JSONObject();
        authorObject.put("text", (footer != null) ? footer.getText() : null);
        authorObject.put("iconUrl", (footer != null) ? footer.getIconUrl() : null);

        object.put("footer", footerObject);

        // Image
        MessageEmbed.ImageInfo image = embed.getImage();
        object.put("imageURL", (image != null) ? image.getUrl() : null);

        return object;
    }
}
