package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.button.SuccessDangerButtonSender;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
import java.util.function.Consumer;

public class AnnouncementCommand extends Command {

    public AnnouncementCommand() {
        super("announcement", "Make an Announcement to a specified channel", new OptionData[]{
                new OptionData(OptionType.CHANNEL, "channel", "Channel to post the Announcement in", true),
                new OptionData(OptionType.STRING, "title", "Title of the announcement-message", true),
                new OptionData(OptionType.STRING, "text", "Text of the announcement-message", true),
                new OptionData(OptionType.ROLE, "pinged-role", "Text of the announcement-message", false)
        }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        Member member = event.getMember();
        MessageChannel senderChannel = event.getChannel();

        // Get args
        MessageChannel targetChannel = event.getOption("channel").getAsMessageChannel();
        String title = event.getOption("title").getAsString();
        String text = event.getOption("text").getAsString();
        OptionMapping roleOption = event.getOption("pinged-role");
        Role pingedRole = roleOption != null ? roleOption.getAsRole() : null;

        // Make sure message is only sent to a TextChannel
        if (!(targetChannel instanceof TextChannel)) {
            event.replyEmbeds(new MyEmbedBuilder().color(Color.RED).title("Error").description("You can only send announcements to TextChannels!").build()).queue();
            return;
        }

        // Create embed
        MyEmbedBuilder builder = new MyEmbedBuilder().color(Color.GRAY).title(title).description(text);
        builder.replace(Main.getPlaceholderTranslator());
        MessageEmbed embed = builder.build();

        Consumer<Member> onSuccess = (member1) -> {
            targetChannel.sendMessageEmbeds(embed).queue();
            if (pingedRole != null)
                targetChannel.sendMessage(pingedRole.getAsMention()).queue();
        }, onCancel = (member1) ->
                senderChannel.sendMessageEmbeds(new MyEmbedBuilder().color(Color.RED).title("Canceled").description("Canceled announcement.").build()).queue();

        SuccessDangerButtonSender sender = new SuccessDangerButtonSender("SEND", onSuccess, "CANCEL", onCancel);

        // Reply to Event
        event.reply("This is your message:").addEmbeds(embed).addActionRow(sender.getButtons()).queue();

        sender.use(member);
    }
}
