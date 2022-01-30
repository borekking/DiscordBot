package de.borekking.bot.command.commands.mute;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.system.InformationProvider;
import de.borekking.bot.time.DurationUtils;
import de.borekking.bot.time.TimeEnum;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute", "Mute a user", new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to mute", true),
                new OptionData(OptionType.STRING, "length", "Ban's length (p for permanent, more info: /validtimes) ", true),
                new OptionData(OptionType.STRING, "reason", "Reason of muting", false)
        }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        // Get options
        String lengthString = event.getOption("length").getAsString();
        Member targetMember = event.getOption("user").getAsMember();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        // Check if user already is muted
        if (Main.getMuteHandler().is(targetMember.getId())) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description(targetMember.getAsMention() + " is already muted.").build()).queue();
            return;
        }

        // Get Mutes's length as millis
        long length;
        try {
            length = DurationUtils.getValue(lengthString);
        } catch (TimeEnum.IllegalDurationException e) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Not a valid Mute length: " + lengthString).build()).queue();
            return;
        }

        boolean successfulMute = this.mute(length, targetMember, reason);

        if (!successfulMute) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Could not mute member " + targetMember.getAsMention() + "\n\n"
                    + "**Possible Reasons**:\n "
                    + "  - MuteRoleID is not valid\n"
                    + "  - MuteRole has a higher or equal priority than the bot´s highest role\n"
                    + "  - Target Member´s highest role has a higher or equal priority as the bot´s highest role").build()).queue();
            return;
        }

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).description("Muted user " + targetMember.getAsMention() + " for " + lengthString + ".").build()).queue();
    }

    private boolean mute(long length, Member member, String reason) {
        return Main.getMuteHandler().use(new InformationProvider(length, member, reason));
    }
}
