package de.borekking.bot.command.commands.mute;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        super("unmute", "description", new OptionData[] {
                new OptionData(OptionType.USER, "user", "User to unmute", true),
                new OptionData(OptionType.STRING, "reason", "Reason of unmuting", false)
        }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        // Get options
        Member targetMember = event.getOption("user").getAsMember();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        if (!Main.getMuteHandler().is(targetMember.getId())) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description(targetMember.getAsMention() + " is not muted.").build()).queue();
            return;
        }

        User user = Main.getMuteHandler().undo(targetMember.getId(), reason);

        // Remove member's roles
        if (user == null) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Could not mute member " + targetMember.getAsMention() + "\n\n"
                    + "**Possible Reasons**:\n "
                    + "  - MuteRoleID is not valid\n"
                    + "  - MuteRole has a higher or equal priority than the bot´s highest role\n"
                    + "  - Target Member´s highest role has a higher or equal priority as the bot´s highest role").build()).queue();
            return;
        }

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).description("Unmuted user " + targetMember.getAsMention() + ".").build()).queue();
    }
}
