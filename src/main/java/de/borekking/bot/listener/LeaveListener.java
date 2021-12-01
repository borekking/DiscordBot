package de.borekking.bot.listener;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.event.EventInformation;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Member member = event.getMember();

        EventInformation information = ConfigSetting.LEAVE_MESSAGE.getAsEventInformation();

        if (information == null) {
            System.err.println("Error on GuildMemberRemoveEvent: EventInformation value of LEAVE_MESSAGE (\"leaveInformation\") is null! Please check your config.");
        } else {
            information.apply(member);
        }
    }
}
