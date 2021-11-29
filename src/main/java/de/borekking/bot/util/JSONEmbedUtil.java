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
     * "color":int
     * "footer": JSONObject with "text":String, "iconUrl":String
     * "author": JSONObject with "name":String, "url":String, "iconUrl":String
     * "imageURL":String
     * "description":String
     * "fields": JSONArray of JSONObjects with "name":String, "value":String, "inline":boolean
     * "title": JSONObject with "title":String, "url":String
     * "timestamp":boolean
     * "thumbnailURL":String
     *
     */

    /*  JSON Example:
           {
              "color": -16711936,
              "footer": {
                "text": "footer text", // text at bottom
                "iconUrl": "https://example/pic0.png" // Image/Icon (circle) left to text
              },
              "author": {
                "iconUrl": "https://example/pic1.png", // Image/Icon (circle) left to name
                "name": "borekking", // Name of author: Right at the top
                "url": "https://youtube.com" // URL on clicking author-name
              },
              "imageURL": "https://example/pic2.png", // Big picture under fields, above footer
              "description": "Welcome to **%servername%**, %user%!%nextLine%You are the %memberCount%. user. :D", // Directly under title
              "fields": [ // List of fields: in middle embed
                {
                "name": "Field!"
                "value": "This is a field"
                "inline": true
                }, // Separated by commas
                {
                "name": "Field 2!"
                "value": "This is another field"
                "inline": true
                }
              ],
              "title": {
                    "title": "Welcome!", // Actual title
                    "url": "https://youtube.com" // URL on clicking author-name
              },
              "timestamp": true, // if timestamp on the right of footer is shown (time when message was sended)
              "thumbnailURL": "https://example/pic3.png" // Image in right, upper corner
           }

            See also: https://b1naryth1ef.github.io/disco/bot_tutorial/message_embeds.html
            Placeholder image: https://socialistmodernism.com/wp-content/uploads/2017/07/placeholder-image.png
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

        // Title
        JSONObject titleObject = (JSONObject) object.get("title");
        String title = (String) titleObject.get("title");
        String url = (String) titleObject.get("url");

        builder.title(title, url);

        // Timestamp
        boolean timestamp = (boolean) object.get("timestamp");
        if (timestamp)
            builder.timestamp(OffsetDateTime.now());

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

        // thumbnail
        String thumbnailURL = (String) object.get("thumbnailURL");
        builder.thumbnail(thumbnailURL);

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
        JSONObject titleObject = new JSONObject();
        titleObject.put("title", embed.getTitle());
        titleObject.put("url", embed.getUrl());

        object.put("title", titleObject);

        // Timestamp
        object.put("timestamp", embed.getTimestamp() != null);

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

        // thumbnail
        MessageEmbed.Thumbnail thumbnail = embed.getThumbnail();
        object.put("thumbnailURL", (thumbnail != null) ? thumbnail.getUrl() : null);

        return object;
    }
}
