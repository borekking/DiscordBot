package de.borekking.bot.command.commands.mute;

import de.borekking.bot.Main;
import de.borekking.bot.command.Command;
import de.borekking.bot.util.discord.embed.EmbedType;
import de.borekking.bot.util.discord.embed.MyEmbedBuilder;
import de.borekking.bot.util.discord.ChannelUtils;
import de.borekking.bot.util.discord.role.RoleBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.Color;
import java.util.Collections;

public class CreateMuteRole extends Command {

    public CreateMuteRole() {
        super("create-mute-role", "Creates a \"mute-role\" with a specified name (No writing for TextChannels/No Joining TextChannels).",
                new OptionData[]{
                        new OptionData(OptionType.STRING, "role-name", "The roles name", true),
                        new OptionData(OptionType.BOOLEAN, "disable-message-sending", "If joining voice channels will be disabled for this role", true),
                        new OptionData(OptionType.BOOLEAN, "disable-voice-joining", "If joining voice channels will be disabled for this role", true)
                }, Permission.ADMINISTRATOR);
    }

    @Override
    public void perform(SlashCommandEvent event) {
        String roleName = event.getOption("role-name").getAsString();
        boolean disableMessageSending = event.getOption("disable-message-sending").getAsBoolean(),
                disableVoiceJoining = event.getOption("disable-voice-joining").getAsBoolean();

        Role role = RoleBuilder.getFrom(roleName, Color.GRAY).create(Main.getDiscordBot().getGuild());

        if (disableMessageSending)
            for (TextChannel channel : Main.getDiscordBot().getGuild().getTextChannels())
                ChannelUtils.setTextChannelPermission(channel, role, Collections.EMPTY_LIST, Permission.getPermissions(Permission.getRaw(Permission.MESSAGE_WRITE)));

        if (disableVoiceJoining)
            for (VoiceChannel voiceChannel : Main.getDiscordBot().getGuild().getVoiceChannels())
                ChannelUtils.setVoiceChannelPermission(voiceChannel, role, Collections.EMPTY_LIST, Permission.getPermissions(Permission.getRaw(Permission.VOICE_CONNECT)));


        event.replyEmbeds(new MyEmbedBuilder(EmbedType.SUCCESS).description("Created mute role \"" + roleName + "\" \n**ID:** " + role.getIdLong()).build()).queue();
    }
}
