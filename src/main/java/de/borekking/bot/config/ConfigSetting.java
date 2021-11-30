package de.borekking.bot.config;

import de.borekking.bot.util.discord.DefaultEmbeds;
import de.borekking.bot.util.discord.JSONEmbedUtil;
import org.json.simple.JSONObject;

public enum ConfigSetting {

    TOKEN(new ConfigPart("token", "")),
    GUILD_ID(new ConfigPart("guildID", "")),
    JOIN_MESSAGE(new ConfigPart("joinInformation", () -> {
        JSONObject embed = JSONEmbedUtil.toJSONObject(DefaultEmbeds.JOIN_EMBED.getEmbed());

        JSONObject object = new JSONObject();
        object.put("enabled", true);
        object.put("channelID", "");
        object.put("embed", embed);

        return object;
    }));

    private ConfigPart part;

    ConfigSetting(ConfigPart part) {
        this.part = part;
    }

    public String getValueAsString() {
        return this.part.getValue(String::valueOf);
    }

    public JSONObject getValueAsJSONObject() {
        return this.part.getValue(o -> (JSONObject) o);
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
