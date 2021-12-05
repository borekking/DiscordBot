package de.borekking.bot.command.commands.ban;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import de.borekking.bot.util.discord.event.EventInformation;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UnbanCommand extends Command {

    public UnbanCommand() {
        super("unban", "Unban a user", new OptionData[]{
                new OptionData(OptionType.STRING, "user", "User to unban as tag or user-id", true),
                new OptionData(OptionType.STRING, "reason", "Reason for unban", false)
        }, Permission.BAN_MEMBERS);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        Guild guild = event.getGuild();

        String userOption = event.getOption("user").getAsString();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        User user = guild.retrieveBanList().complete().stream().map(Guild.Ban::getUser)
                .filter(tempUser -> userOption.equals(tempUser.getAsTag()) || userOption.equals(tempUser.getId())).findAny().orElse(null);

        if (user == null) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Could not find a banned user with tag or id " + userOption).build()).queue();
            return;
        }

        guild.unban(user).queue();
        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).title("Unban").description("Successfully unbanned " + user.getAsMention() + ".").build()).queue();

        EventInformation information = ConfigSetting.UNBAN_PLAYER_MESSAGE.getAsEventInformation();
        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of UNBAN_PLAYER_MESSAGE (\"unbanInformation\") is null! Please check your config.");
        } else {
            information.apply(user, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
        }
    }
}
