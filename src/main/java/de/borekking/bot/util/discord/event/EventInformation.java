package de.borekking.bot.util.discord.event;

import de.borekking.bot.Main;
import de.borekking.bot.config.JSONAble;
import de.borekking.bot.util.discord.JSONEmbedUtil;
import de.borekking.bot.util.discord.MyEmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
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

    public void apply(Member member) {
        if (this.undefined) return;
        if (!this.enabled) return;

        TextChannel channel = Main.getDiscordBot().getGuild().getTextChannelById(this.channelID);
        if (channel == null) return;

        MyEmbedBuilder embedCopy = this.embed.copy();
        embedCopy.replace(Main.getPlaceholderTranslator(), member);

        channel.sendMessageEmbeds(embedCopy.build()).queue();
    }

    @Override
    public JSONObject toJSONObject() {
        if (this.jsonObject != null) return jsonObject;
        this.jsonObject = new JSONObject();

        this.jsonObject.put("enabled", this.enabled);
        this.jsonObject.put("channelID", this.channelID);
        this.jsonObject.put("embed", JSONEmbedUtil.toJSONObject(this.embed.build()));

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
}

