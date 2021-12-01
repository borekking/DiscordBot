package de.borekking.bot.util.discord.event;

import de.borekking.bot.config.JSONAble;
import de.borekking.bot.util.discord.JSONEmbedUtil;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.simple.JSONObject;

public class EventInformation implements JSONAble {

    private final boolean enabled;
    private final String channelID;
    private final MessageEmbed embed;
    private JSONObject jsonObject;

    public EventInformation(boolean enabled, String channelID, MessageEmbed embed) {
        this.enabled = enabled;
        this.channelID = channelID;
        this.embed = embed;
    }

    @Override
    public JSONObject toJSONObject() {
        if (this.jsonObject != null) return jsonObject;
        this.jsonObject = new JSONObject();

        this.jsonObject.put("enabled", this.enabled);
        this.jsonObject.put("channelID", this.channelID);
        this.jsonObject.put("embed", this.embed);

        return this.jsonObject;
    }

    public static EventInformation getFromJSON(JSONObject object) {
        if (object == null) return null;

        Boolean enabled = (Boolean) object.get("enabled");
        String channelID = (String) object.get("channelID");
        JSONObject jsonEmbed = (JSONObject) object.get("embed");
        if (enabled == null || channelID == null || jsonEmbed == null) return null;

        return new EventInformation(enabled, channelID, JSONEmbedUtil.toMyEmbedBuilder(jsonEmbed).build());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public MessageEmbed getEmbed() {
        return embed;
    }

    public String getChannelID() {
        return channelID;
    }
}
