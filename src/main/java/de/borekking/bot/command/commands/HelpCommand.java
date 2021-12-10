package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.time.OffsetDateTime;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Information about the bot's commands", new OptionData[0]);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        MyEmbedBuilder builder = new MyEmbedBuilder(EmbedType.NEUTRAL).title("Commands Help").timestamp(OffsetDateTime.now());

        for (Command command : Main.getDiscordBot().getCommandList())
            if (event.getMember().hasPermission(command.getPermissions()))
                builder.field(command.getName(), command.getDescription(), false);

        // USer has no permissions for any commands
        if (builder.build().getFields().size() == 0)
            builder.description("Cringe, don't being able to use any commands");


        event.replyEmbeds(builder.build()).queue();
    }
}
