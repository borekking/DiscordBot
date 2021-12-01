package de.borekking.bot.config;

import de.borekking.bot.util.discord.DefaultEmbeds;
import de.borekking.bot.util.discord.event.EventInformation;
import org.json.simple.JSONObject;

public enum ConfigSetting {

    TOKEN(new ConfigPart("token", "")),
    GUILD_ID(new ConfigPart("guildID", "")),
    JOIN_MESSAGE(new ConfigPart("joinInformation", new EventInformation(true, "", DefaultEmbeds.JOIN_EMBED.getEmbed()).toJSONObject())),
    LEAVE_MESSAGE(new ConfigPart("leaveInformation", new EventInformation(true, "", DefaultEmbeds.LEAVE_EMBED.getEmbed()).toJSONObject()));

    private final ConfigPart part;

    ConfigSetting(ConfigPart part) {
        this.part = part;
    }

    public String getValueAsString() {
        return this.part.getValue(String::valueOf);
    }

    public JSONObject getValueAsJSONObject() {
        return this.part.getValue(o -> o instanceof JSONObject ? (JSONObject) o : null);
    }

    public EventInformation getAsEventInformation() {
        return this.part.getValue(o -> EventInformation.getFromJSON(this.getValueAsJSONObject()));
    }

    public String getKey() {
        return this.part.getKey();
    }

    public Object getValue() {
        return this.part.getValue();
    }

    public void setValue(Object value) {
        this.part.setValue(value);
    }
}
