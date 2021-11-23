package de.borekking.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private JDA jda;
    private JDABuilder builder;
    private Guild guild;
    private final String token;

    public DiscordBot(String token, String guildID, Activity activity) throws LoginException {
        this.token = token;

        this.builder = JDABuilder.createLight(this.token);
        this.builder.setStatus(OnlineStatus.ONLINE);
        this.builder.setActivity(activity);

        this.builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        this.builder.setChunkingFilter(ChunkingFilter.ALL);
        this.builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        this.jda = this.builder.build();
        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.guild = this.jda.getGuildById(guildID);
    }

    public void disableBot() {
        if (this.jda != null) {
            this.jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            this.jda.shutdown();
            System.exit(0);
        }
    }
}
