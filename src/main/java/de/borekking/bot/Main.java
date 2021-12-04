package de.borekking.bot;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.config.ConfigurationManager;
import de.borekking.bot.util.discord.button.ButtonManager;
import de.borekking.bot.util.discord.Timestamp;
import de.borekking.bot.util.placeholder.PlaceholderManager;
import de.borekking.bot.util.placeholder.PlaceholderTranslator;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.placeholder.placeholderTypes.MemberPlaceholder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    /*
     * TODO:
     * Add...
     *  - UnbanCommand
     *
     */

    /*
     * Placeholders
     *
     * - %user% user that joined as mention
     * - %memberCount% amount of members on Server
     * - %servername% server's name
     * - %date% date in format mm/dd/yyyy
     * - %nextLine% next line
     *
     * Extras:
     * - %reason% for actions like ban/warn/mute
     *
     */

    private static ConfigurationManager configurationManager;
    private static DiscordBot discordBot;
    private static PlaceholderTranslator placeholderTranslator;
    private static ButtonManager buttonManager;

    public static void main(String[] args) throws IOException, ParseException {
        configurationManager = new ConfigurationManager("config.json");
        buttonManager = new ButtonManager();

        load();

        placeholderTranslator = new PlaceholderTranslator(getGeneralPlaceholderManager(), getMemberPlaceholderManager());

        startConsoleListener();
    }

    public static void reload() throws IOException, ParseException {
        discordBot.disableBot();
        buttonManager.clear();
        load();
    }

    private static void load() throws IOException, ParseException {
        configurationManager.load();

        try {
            discordBot = new DiscordBot(ConfigSetting.TOKEN.getValueAsString(), ConfigSetting.GUILD_ID.getValueAsString(),
                    getActivity());
        } catch (LoginException e) {
            System.err.println("Error while connecting DiscordBot!");
            System.exit(0);
        }
    }

    private static Activity getActivity() {
        String[] activities = new String[]{"playing", "streaming", "listening", "watching", "", "competing"};
        JSONObject setting = ConfigSetting.Activity.getValueAsJSONObject();

        for (int i = 0; i < activities.length; i++) {
            String activity = activities[i];
            if (activity.isEmpty()) continue;

            JSONObject object = (JSONObject) setting.get(activity);
            boolean enabled = (boolean) object.get("enabled");

            if (enabled) {
                String name = (String) object.get("name");
                String url = (String) object.get("url");

                Activity.ActivityType activityType = Activity.ActivityType.fromKey(i);
                return Activity.of(activityType, name, url);
            }
        }

        return Activity.playing("187");
    }

    private static PlaceholderManager<Void> getGeneralPlaceholderManager() {
        PlaceholderManager<Void> manager = new PlaceholderManager<>();

        manager.addPlaceholder(new GeneralPlaceholder("%nextLine%", () -> "\n"));
        manager.addPlaceholder(new GeneralPlaceholder("%memberCount%", () -> String.valueOf(Main.getDiscordBot().getGuild().getMemberCount())));
        manager.addPlaceholder(new GeneralPlaceholder("%servername%", () -> discordBot.getGuild().getName()));
        manager.addPlaceholder(new GeneralPlaceholder("%date%", Timestamp.SHORT_DATE::apply));

        return manager;
    }

    private static PlaceholderManager<Member> getMemberPlaceholderManager() {
        PlaceholderManager<Member> manager = new PlaceholderManager<>();
        manager.addPlaceholder(new MemberPlaceholder("%user%", IMentionable::getAsMention));
        return manager;
    }

    public static ButtonManager getButtonManager() {
        return buttonManager;
    }

    public static PlaceholderTranslator getPlaceholderTranslator() {
        return placeholderTranslator;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

    private static void startConsoleListener() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String str = scanner.nextLine();

            switch (str) {
                case "exit":
                case "stop":
                    discordBot.disableBot();
                    break;
                case "start":
                    load();
                    break;
                case "reload":
                    reload();
                    break;
            }
        }
    }
}
