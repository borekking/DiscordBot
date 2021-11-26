package de.borekking.bot.config;

import org.json.simple.JSONObject;

public enum ConfigSetting {

    TOKEN(new ConfigPart("token", "")),
    GUILD_ID(new ConfigPart("guildID", "")),
    JOIN_MESSAGE(new ConfigPart("joinInformation", null));

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
