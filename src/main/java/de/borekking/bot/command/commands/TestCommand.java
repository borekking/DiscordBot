package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", "Test the bot", new OptionData[0], Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        long ping = Main.getDiscordBot().getPing();
        MyEmbedBuilder builder = new MyEmbedBuilder().color(Color.GREEN).title("Online").description("The bot works :)%nextLine%%nextLine%Ping: " + ping);
        builder.replace(Main.getPlaceholderTranslator());
        event.replyEmbeds(builder.build()).queue();
    }
}
