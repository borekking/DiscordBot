package de.borekking.bot.listener;

import de.borekking.bot.Main;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.MyEmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;

public class JoinListener extends ListenerAdapter {

    /*
     * Adds following placeholders:
     * - %user% user that joined
     * - %memberCount% amount of members on Server
     * - %servername% server's name
     *
     */

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

        String text = (String) jsonObject.get("text");
        String header = (String) jsonObject.get("title");
        header = this.translateMessage(header, member);
        text = this.translateMessage(text, member);

        channel.sendMessageEmbeds(new MyEmbedBuilder().title(header).description(text).build()).queue();
    }

    private String translateMessage(String str, Member member) {
        int memberCount = Main.getDiscordBot().getGuild().getMemberCount();
        return str.replaceAll("%user%", member.getAsMention())
                .replaceAll("%memberCount%", String.valueOf(memberCount))
                .replaceAll("%servername%", Main.getDiscordBot().getGuild().getName());
    }
}
