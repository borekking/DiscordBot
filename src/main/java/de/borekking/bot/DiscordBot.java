package de.borekking.bot;

import de.borekking.bot.command.Command;
import de.borekking.bot.command.commands.ExitCommand;
import de.borekking.bot.listener.JoinListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordBot {

    private final JDA jda;
    private final Guild guild;
    private final List<Command> commandList;

    public DiscordBot(String token, String guildID, Activity activity) throws LoginException {
        this.commandList = commandList();

        JDABuilder builder = JDABuilder.createLight(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(activity);

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        this.jda = builder.build();

        this.registerCommands(this.jda.updateCommands());

        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    private void registerCommands(CommandListUpdateAction commands) {
        commands.addCommands(this.commandList.stream().map(Command::getCommandData).collect(Collectors.toList())).complete();
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
