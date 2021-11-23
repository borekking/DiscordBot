package de.borekking.bot;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.config.ConfigurationManager;
import net.dv8tion.jda.api.entities.Activity;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    private static DiscordBot discordBot;

    public static void main(String[] args) throws IOException, ParseException {
        new ConfigurationManager("config.json");

        try {
            discordBot = new DiscordBot(ConfigSetting.TOKEN.getValueAsString(), ConfigSetting.GUILD_ID.getValueAsString(),
                    Activity.playing("187"));
        } catch (LoginException e) {
            System.err.println("Error while connecting DiscordBot!");
            System.exit(0);
        }
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }
}
