package de.borekking.bot.config;

import de.borekking.bot.util.discord.embed.DefaultEmbeds;
import de.borekking.bot.util.discord.event.EventInformation;
import org.json.simple.JSONObject;

public enum ConfigSetting {

    TOKEN(new ConfigPart("token", "")),
    GUILD_ID(new ConfigPart("guildID", "")),
    JOIN_MESSAGE(new ConfigPart("joinInformation", new EventInformation(true, "", DefaultEmbeds.JOIN_EMBED.getEmbed()).toJSONObject())),
    LEAVE_MESSAGE(new ConfigPart("leaveInformation", new EventInformation(true, "", DefaultEmbeds.LEAVE_EMBED.getEmbed()).toJSONObject())),
    Activity(new ConfigPart("activities", () -> {
        JSONObject main = new JSONObject();
        // Adding "normal" ones
        for (String activity : new String[] {"competing", "listening", "playing", "watching", "streaming"}) {
            JSONObject object = new JSONObject();
            object.put("enabled", false);
            object.put("name", "");
            // Adding URL for "streaming"
            if (activity.equals("streaming"))
                object.put("url", null);
            main.put(activity, object);
        }
        return main;
    })),
    BANNED_PLAYER_MESSAGE(new ConfigPart("banInformation", new EventInformation(true, "", DefaultEmbeds.BAN_INFORMATION.getEmbed()).toJSONObject())),
    UNBAN_PLAYER_MESSAGE(new ConfigPart("unbanInformation", new EventInformation(true, "", DefaultEmbeds.UNBAN_INFORMATION.getEmbed()).toJSONObject()));

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
