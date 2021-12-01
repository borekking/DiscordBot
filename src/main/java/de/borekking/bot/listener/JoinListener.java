package de.borekking.bot.listener;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.event.EventInformation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();

        EventInformation information = ConfigSetting.JOIN_MESSAGE.getAsEventInformation();

        if (information == null) {
            System.err.println("Error on GuildMemberJoinEvent: EventInformation value of JOIN_MESSAGE (\"joinInformation\") is null! Please check your config.");
        } else {
            information.apply(member);
        }
    }
}
