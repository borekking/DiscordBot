package de.borekking.bot;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.config.ConfigurationManager;
import de.borekking.bot.util.discord.Timestamp;
import de.borekking.bot.util.placeholder.PlaceholderManager;
import de.borekking.bot.util.placeholder.PlaceholderTranslator;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.placeholder.placeholderTypes.MemberPlaceholder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    /*
     * Placeholders
     *
     * - %user% user that joined as mention
     * - %memberCount% amount of members on Server
     * - %servername% server's name
     * - %date% date in format mm/dd/yyyy
     * - %nextLine% next line
     *
     */

    private static DiscordBot discordBot;
    private static PlaceholderTranslator placeholderTranslator;

    public static void main(String[] args) throws IOException, ParseException {
        new ConfigurationManager("config.json");

        try {
            discordBot = new DiscordBot(ConfigSetting.TOKEN.getValueAsString(), ConfigSetting.GUILD_ID.getValueAsString(),
                    Activity.playing("187"));
        } catch (LoginException e) {
            System.err.println("Error while connecting DiscordBot!");
            System.exit(0);
        }

        placeholderTranslator = new PlaceholderTranslator(getGeneralPlaceholderManager(), getMemberPlaceholderManager());
    }

    private static PlaceholderManager<Void> getGeneralPlaceholderManager() {
        int memberCount = Main.getDiscordBot().getGuild().getMemberCount();

        PlaceholderManager<Void> manager = new PlaceholderManager<>();
        manager.addPlaceholder(new GeneralPlaceholder("%nextLine%", "\n"));
        manager.addPlaceholder(new GeneralPlaceholder("%memberCount%", String.valueOf(memberCount)));
        manager.addPlaceholder(new GeneralPlaceholder("%servername%", discordBot.getGuild().getName()));
        manager.addPlaceholder(new GeneralPlaceholder("%date%", Timestamp.SHORT_DATE.apply()));

        return manager;
    }

    private static PlaceholderManager<Member> getMemberPlaceholderManager() {
        PlaceholderManager<Member> manager = new PlaceholderManager<>();
        manager.addPlaceholder(new MemberPlaceholder("%user%", IMentionable::getAsMention));
        return manager;
    }

    public static PlaceholderTranslator getPlaceholderTranslator() {
        return placeholderTranslator;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }
}
