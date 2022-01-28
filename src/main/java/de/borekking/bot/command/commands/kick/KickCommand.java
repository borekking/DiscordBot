package de.borekking.bot.command.commands.kick;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import de.borekking.bot.util.discord.event.EventInformation;
import de.borekking.bot.placeholder.placeholderTypes.GeneralPlaceholder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", "Kick a User", new OptionData[] {
                new OptionData(OptionType.USER, "user", "User to kick", true),
                new OptionData(OptionType.STRING, "reason", "Reason of kick", false)
        }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        Member member = event.getMember();

        Member targetMember = event.getOption("user").getAsMember();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        // Check member banning himself
        if (member.equals(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Why would you kick yourself?").build()).queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();

        if (selfMember.equals(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Please don't kick me :(").build()).queue();
            return;
        }

        // Check if targetMember can be interacted
        if (!selfMember.canInteract(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("I don't have the required permission to kick " + targetMember.getAsMention() + "!").build()).queue();
            return;
        }

        // Make sure reason isn't longer than 512 characters
        if (reason.length() > 512) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Reason can not be longer than 512 characters!").build()).queue();
            return;
        }

        // Actual kicking
        targetMember.kick(reason).queue();

        // Replying to Event
        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).title("Kick").description("Kicked " + targetMember.getAsMention()).build()).queue();

        // Apply EventInformation (from config)
        EventInformation information = ConfigSetting.KICK_PLAYER_MESSAGE.getAsEventInformation();
        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of KICK_PLAYER_MESSAGE (\"kickInformation\") is null! Please check your config.");
        } else {
            information.apply(targetMember, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
        }
    }
}
