package de.borekking.bot.listener;

import de.borekking.bot.Main;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.JSONEmbedUtil;
import de.borekking.bot.util.discord.MyEmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        JSONObject jsonObject = ConfigSetting.JOIN_MESSAGE.getValueAsJSONObject();

        if (jsonObject == null) {
            System.err.println("Error on GuildMemberJoinEvent: JSONObject value of JOIN_MESSAGE (\"joinInformation\") is null! Please check your config.");
            return;
        }

        boolean enabled = (boolean) jsonObject.get("enabled");
        if (!enabled) return;

        String channelID = (String) jsonObject.get("channelID");
        TextChannel channel = event.getGuild().getTextChannelById(channelID);
        if (channel == null) return;

        JSONObject embed = (JSONObject) jsonObject.get("embed");
        MyEmbedBuilder builder = JSONEmbedUtil.toMyEmbedBuilder(embed);
        builder.replace(Main.getPlaceholderTranslator(), member);

        channel.sendMessageEmbeds(builder.build()).queue();
    }
}
