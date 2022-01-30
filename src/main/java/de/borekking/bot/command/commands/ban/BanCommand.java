package de.borekking.bot.command.commands.ban;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.time.DurationUtils;
import de.borekking.bot.time.TimeEnum;
import de.borekking.bot.util.discord.button.ConfirmationButtonSender;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.function.Consumer;

public class BanCommand extends Command {

    public BanCommand() {
        super("ban", "Ban a user", new OptionData[]{
                new OptionData(OptionType.USER, "user", "User to ban", true),
                new OptionData(OptionType.STRING, "length", "Ban's length (p for permanent, more info: /validtimes)", true),
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
        String lengthString = event.getOption("length").getAsString();
        OptionMapping reasonOption = event.getOption("reason");
        String reason = reasonOption != null ? reasonOption.getAsString() : "No reason provided";
        OptionMapping delDaysOption = event.getOption("del-days");
        int delDays = delDaysOption != null ? (int) delDaysOption.getAsLong() : 0;

        // Check member banning himself
        if (member.equals(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Why would you ban yourself?").build()).queue();
            return;
        }

        Member selfMember = event.getGuild().getSelfMember();

        if (selfMember.equals(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Please don't ban me :(").build()).queue();
            return;
        }

        // Check if targetMember can be interacted
        if (!selfMember.canInteract(targetMember)) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("I don't have the required permission to ban " + targetMember.getAsMention() + "!").build()).queue();
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

        // Get Ban's length as millis
        long length;
        try {
            length = DurationUtils.getValue(lengthString);
        } catch (TimeEnum.IllegalDurationException e) {
            event.replyEmbeds(new MyEmbedBuilder(EmbedType.ERROR).description("Not a valid Ban length: " + lengthString).build()).queue();
            return;
        }

        // Get ConfirmationButton
        ConfirmationButtonSender buttonSender = this.createConfirmationButton(targetMember, channel, reason, length, delDays);

        event.replyEmbeds(new MyEmbedBuilder(EmbedType.NEUTRAL).description("Are you sure to ban " + targetMember.getAsMention()
                + " for " + lengthString + ", for the reason \"" + reason + "\" (" + delDays + " delDays)?").title("Confirmation").build()).addActionRow(buttonSender.getButtons()).queue();

        buttonSender.use(member);
    }

    // Unban the Member
    private boolean ban(Member member, String reason, long length, int delDays) {
        return Main.getBanHandler().ban(member, reason, length, delDays);
    }

    private ConfirmationButtonSender createConfirmationButton(Member member, TextChannel channel, String reason, long length, int delDays) {
        // Consumer for success (ban) and cancel (no ban and cancel message)
        Consumer<Member> onSuccess = (member1) -> this.ban(member, reason, length, delDays);
        Consumer<Member> onCancel = (member1) -> channel.sendMessageEmbeds(new MyEmbedBuilder().color(Color.RED).title("Canceled").description("Canceled ban.").build()).queue();

        // Create SuccessDangerButtonSender
        return new ConfirmationButtonSender("BAN", onSuccess, "CANCEL", onCancel);
    }
}
