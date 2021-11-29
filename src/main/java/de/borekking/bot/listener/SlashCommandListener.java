package de.borekking.bot.listener;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.MyEmbedBuilder;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;

        Member member = event.getMember();

        String name = event.getName();

        Command command = Main.getDiscordBot().getCommandByName(name);

        if (!member.hasPermission(command.getPermissions())) {
            this.sendNoPermissions(event);
            return;
        }

        command.perform(event);
    }

    private void sendNoPermissions(SlashCommandEvent event) {
        event.replyEmbeds(new MyEmbedBuilder().color(Color.RED).title("ERROR").description("You don´t have the required permissions to do that!").build()).complete();
    }
}
