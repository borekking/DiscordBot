package de.borekking.bot.command.commands;

import de.borekking.bot.command.Command;
import de.borekking.bot.time.TimeEnum;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
import java.util.Arrays;

public class ValidTimeCommand extends Command {

    public ValidTimeCommand() {
        super("validtimes", "List of valid time formats", new OptionData[0], Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        event.replyEmbeds(this.getInfo()).queue();
    }

    private MessageEmbed getInfo() {
        MyEmbedBuilder builder = new MyEmbedBuilder().color(Color.GRAY).title("Valid Times");

        Arrays.stream(TimeEnum.values()).forEach(time -> builder.field(time.getName() + ": ", time.getShortName(), false));

        return builder.build();
    }
}
