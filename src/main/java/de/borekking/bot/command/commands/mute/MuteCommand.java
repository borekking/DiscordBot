package de.borekking.bot.command.commands.mute;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.role.RoleUtils;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import de.borekking.bot.util.discord.event.EventInformation;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.simple.JSONObject;

public class MuteCommand extends Command {

    // TODO Add time

    public MuteCommand() {
        super("mute", "Mute a user", new OptionData[] {
                new OptionData(OptionType.USER, "user", "User to mute", true),
                new OptionData(OptionType.STRING, "reason", "Reason of muting", false)
        }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        // Get options
        Member targetMember = event.getOption("user").getAsMember();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        Role muteRole = Main.getMuteRole();

        // Check if user already is muted
        if (RoleUtils.hasRoles(targetMember, muteRole)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description(targetMember.getAsMention() + " is already muted.").build()).queue();
            return;
        }

        if (!RoleUtils.addRoles(targetMember, muteRole)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Could not mute member " + targetMember.getAsMention() + "\n\n"
                    + "**Possible Reasons**:\n "
                    + "  - MuteRoleID is not valid\n"
                    + "  - MuteRole has a higher or equal priority than the bot´s highest role\n"
                    + "  - Target Member´s highest role has a higher or equal priority as the bot´s highest role").build()).queue();
            return;
        }

        EventInformation information = EventInformation.getFromJSON((JSONObject) ConfigSetting.MUTES.getInnerValue("muteInformation"));
        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of MUTES.muteInformation (\"mute.muteInformation\") is null! Please check your config.");
        } else {
            information.apply(targetMember, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
        }

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).description("Muted user " + targetMember.getAsMention() + ".").build()).queue();
    }
}
