package de.borekking.bot.command.commands;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.listener.button.ButtonAction;
import de.borekking.bot.listener.button.ButtonManager;
import de.borekking.bot.util.discord.MyEmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.Color;

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

        // Create buttons
        ButtonManager manager = Main.getButtonManager();
        long id = manager.getNewUniqueID();
        String[] buttonID = manager.getIDs(id, 2);
        Button sendButton = Button.success(buttonID[0], "SEND");
        Button cancelButton = Button.success(buttonID[1], "CANCEL");

        // Reply to Event
        event.reply("This is your message:").addEmbeds(embed).addActionRow(sendButton, cancelButton).queue();

        // Create ButtonAction
        ButtonAction buttonAction = new ButtonAction(id);
        buttonAction.addAction(sendButton, (member1) -> {
            targetChannel.sendMessageEmbeds(embed).queue();
            if (pingedRole != null)
                targetChannel.sendMessage(pingedRole.getAsMention()).queue();
        });
        buttonAction.addAction(cancelButton, (member1) ->
                senderChannel.sendMessageEmbeds(new MyEmbedBuilder().color(Color.RED).title("Canceled").description("Canceled announcement.").build()).queue());
        Main.getButtonManager().addButtonEvent(member, buttonAction);
    }
}
