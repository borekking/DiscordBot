package de.borekking.bot.command.commands.ban;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;

import net.dv8tion.jda.api.Permission;
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
        String userOption = event.getOption("user").getAsString();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption == null ? "No reason provided" : reasonOption.getAsString();

        User user = this.unban(userOption, reason);

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).title("Unban").description("Successfully unbanned " + user.getAsMention() + ".").build()).queue();
    }

    // Unban the User
    private User unban(String userID, String reason) {
        return Main.getBanHandler().undo(userID, reason);
    }
}
