package de.borekking.bot.command.commands.ban;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.util.discord.button.SuccessDangerButtonSender;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import de.borekking.bot.util.discord.event.EventInformation;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
import java.util.function.Consumer;

public class BanCommand extends Command {

    // TODO at time

    public BanCommand() {
        super("ban", "Ban a user", new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to ban", true),
                new OptionData(OptionType.STRING, "reason", "Reason for ban", false),
                new OptionData(OptionType.INTEGER, "del-days", "All messages up to this will be deleted", false)
        }, Permission.BAN_MEMBERS);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getTextChannel();

        // Get Options
        Member targetMember = event.getOption("user").getAsMember();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption != null ? reasonOption.getAsString() : "No reason provided";
        OptionMapping delDaysOption = event.getOption("del-days");
        int delDays = delDaysOption != null ? (int) delDaysOption.getAsLong() : 0;

        // Check member banning himself
        if (member.equals(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Why would you ban yourself?").build()).queue();
            return;
        }

        // Check if targetMember can be interacted
        if (!event.getGuild().getSelfMember().canInteract(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("The bot can not ban " + targetMember.getAsMention() + "!").build()).queue();
            return;
        }

        // Make sure reason isn't longer than 512 characters
        if (reason.length() > 512) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Reason can not be longer than 512 characters!").build()).queue();
            return;
        }

        // Make sure delDays is between 0 (inclusive) and 7 (inclusive)
        if (delDays < 0) delDays = 0;
        else if (delDays > 7) delDays = 7;

        // Consumer for success (ban) and cancel
        int finalDelDays = delDays;
        Consumer<Member> onSuccess = (member1) -> {
            targetMember.ban(finalDelDays, reason).queue();

            EventInformation information = ConfigSetting.BANNED_PLAYER_MESSAGE.getAsEventInformation();
            if (information == null) {
                System.err.println("Error on SlashCommandEvent: EventInformation value of BANNED_PLAYER_MESSAGE (\"banInformation\") is null! Please check your config.");
            } else {
                information.apply(targetMember, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
            }
        }, onCancel = (member1) ->
                channel.sendMessageEmbeds(new MyEmbedBuilder().color(Color.RED).title("Canceled").description("Canceled ban.").build()).queue();

        // Create SuccessDangerButtonSender
        SuccessDangerButtonSender sender = new SuccessDangerButtonSender("BAN", onSuccess, "CANCEL", onCancel);

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.NEUTRAL).description("Are you sure to ban " + targetMember.getAsMention()
                + " for the reason \"" + reason + "\" (" + delDays + " delDays)?").title("Confirmation").build()).addActionRow(sender.getButtons()).queue();

        sender.use(member);
    }
}
