package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.DefaultEmbeds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", "Reload the config and restarts the bot.", new OptionData[0], Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        event.replyEmbeds(DefaultEmbeds.RELOAD_EMBED.getEmbed().build()).complete();

        try {
            Main.reload();
        } catch (IOException | ParseException ignored) {
            // No reply due bot is offline if fail
            System.err.println("Could not restart bot after reload./ Could not read config.");
        }
    }
}
