package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.DefaultEmbeds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "Disables Bot", new OptionData[0], Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        event.replyEmbeds(DefaultEmbeds.EXIT_EMBED.getEmbed().build()).complete();
        Main.exit();
    }
}
