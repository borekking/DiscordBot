package de.borekking.bot.util.discord.event;

import de.borekking.bot.config.JSONAble;
import de.borekking.bot.util.discord.JSONEmbedUtil;
import de.borekking.bot.util.discord.MyEmbedBuilder;
import org.json.simple.JSONObject;

public class EventInformation implements JSONAble {

    private final boolean enabled, undefined;
    private final String channelID;
    private final MyEmbedBuilder embed;
    private JSONObject jsonObject;

    public EventInformation(boolean enabled, String channelID, MyEmbedBuilder embed) {
        this.enabled = enabled;
        this.channelID = channelID;
        this.embed = embed;

        this.undefined = this.channelID == null || this.embed == null || this.embed.build().isEmpty();
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

        return new EventInformation(enabled, channelID, JSONEmbedUtil.toMyEmbedBuilder(jsonEmbed));
    }

    public boolean isUndefined() {
        return undefined;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public MyEmbedBuilder getEmbed() {
        return embed;
    }

    public String getChannelID() {
        return channelID;
    }
}
