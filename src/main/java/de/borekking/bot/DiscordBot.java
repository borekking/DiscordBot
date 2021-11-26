package de.borekking.bot;

import de.borekking.bot.command.Command;
import de.borekking.bot.command.commands.ExitCommand;
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

        this.registerCommands(this.jda.updateCommands());

        this.guild = this.jda.getGuildById(guildID);
    }

    public Command getCommandByName(String name) {
        return commandList.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void disableBot() {
        if (this.jda != null) {
            this.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            this.jda.shutdown();
            System.exit(0);
        }
    }

    private void registerListeners() {
        this.registerEvent(new SlashCommandListener());
    }

    private void registerEvent(ListenerAdapter e) {
        this.builder.addEventListeners(e);
    }

    private void registerCommands(CommandListUpdateAction commands) {
        for (Command command : this.commandList)
            commands.addCommands(command.getCommandData()).queue();
        commands.queue();
    }

    private List<Command> commandList() {
        List<Command> list = new ArrayList<>();
        list.add(new ExitCommand());
        return list;
    }

    public Guild getGuild() {
        return guild;
    }
}
