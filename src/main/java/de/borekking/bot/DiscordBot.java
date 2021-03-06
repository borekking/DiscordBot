package de.borekking.bot;

import de.borekking.bot.command.Command;
import de.borekking.bot.command.commands.AnnouncementCommand;
import de.borekking.bot.command.commands.ExitCommand;
import de.borekking.bot.command.commands.HelpCommand;
import de.borekking.bot.command.commands.ReloadCommand;
import de.borekking.bot.command.commands.TestCommand;
import de.borekking.bot.command.commands.ValidTimeCommand;
import de.borekking.bot.command.commands.ban.BanCommand;
import de.borekking.bot.command.commands.ban.UnbanCommand;
import de.borekking.bot.command.commands.kick.KickCommand;
import de.borekking.bot.command.commands.mute.CreateMuteRole;
import de.borekking.bot.command.commands.mute.MuteCommand;
import de.borekking.bot.command.commands.mute.UnmuteCommand;
import de.borekking.bot.listener.ButtonClickListener;
import de.borekking.bot.listener.JoinListener;
import de.borekking.bot.listener.LeaveListener;
import de.borekking.bot.listener.SlashCommandListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot {

    private final JDABuilder builder;
    private final JDA jda;
    private final Guild guild;
    private final List<Command> commandList;

    public DiscordBot(String token, String guildID, Activity activity) throws LoginException {
        this.commandList = commandList();

        this.builder = JDABuilder.createLight(token);
        this.builder.setStatus(OnlineStatus.ONLINE);
        this.builder.setActivity(activity);

        this.builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        this.builder.setChunkingFilter(ChunkingFilter.ALL);
        this.builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        this.registerListeners();

        this.jda = this.builder.build();

        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.guild = this.jda.getGuildById(guildID);

        this.registerCommands(this.guild.updateCommands());
    }

    public Command getCommandByName(String name) {
        return commandList.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void disableBot() {
        if (this.jda != null) {
            this.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            this.jda.shutdown();
        }
    }

    public long getPing() {
        return this.jda.getGatewayPing();
    }

    private void registerListeners() {
        this.registerEvent(new SlashCommandListener());
        this.registerEvent(new JoinListener());
        this.registerEvent(new LeaveListener());
        this.registerEvent(new ButtonClickListener());
    }

    private void registerEvent(ListenerAdapter e) {
        this.builder.addEventListeners(e);
    }

    private void registerCommands(CommandListUpdateAction commands) {
        for (Command command : this.commandList)
            commands.addCommands(command.getCommandData());
        commands.queue();
    }

    private List<Command> commandList() {
        List<Command> list = new  ArrayList<>();
        list.add(new ExitCommand());
        list.add(new ReloadCommand());
        list.add(new AnnouncementCommand());
        list.add(new TestCommand());
        list.add(new BanCommand());
        list.add(new UnbanCommand());
        list.add(new HelpCommand());
        list.add(new KickCommand());
        list.add(new MuteCommand());
        list.add(new UnmuteCommand());
        list.add(new CreateMuteRole());
        list.add(new ValidTimeCommand());
        return list;
    }

    public List<Command> getCommandList() {
        return commandList;
    }

    public Guild getGuild() {
        return guild;
    }
}
