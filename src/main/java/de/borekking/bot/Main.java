package de.borekking.bot;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.config.ConfigurationManager;
import de.borekking.bot.util.discord.Timestamp;
import de.borekking.bot.util.discord.button.ButtonManager;
import de.borekking.bot.util.placeholder.PlaceholderManager;
import de.borekking.bot.util.placeholder.PlaceholderTranslator;
import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.placeholder.placeholderTypes.UserPlaceholder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    /*
     * TODO:
     *  - Make all Action Commands with deactivatable (config) Conformations
     *  - Add PermissionSystem:
     *     - Saved in Database
     *     - You can define roles with a command:
     *        role create <name> [<permission_1>, <permission_2>, ...]
     *     - You can remove roles roles:
     *        role remove <name>
     *     - You can list all roles:
     *        role list
     *     - Permissions:
     *        - Strings
     *        - Documented List for all Commands
     *        - * for all permissions
     *
     *  TODAY:
     *   - Ticket System (start)
     *   - Add Time to Muting and Baning
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

    private static Role muteRole;
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

    public static void exit() {
        discordBot.disableBot();
        System.exit(0);
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

        String muteRoleID = (String) ConfigSetting.MUTES.getInnerValue("muteRoleID");
        muteRole = muteRoleID != null && !muteRoleID.trim().isEmpty() ? discordBot.getGuild().getRoleById(muteRoleID) : null;
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

    private static PlaceholderManager<User> getMemberPlaceholderManager() {
        PlaceholderManager<User> manager = new PlaceholderManager<>();
        manager.addPlaceholder(new UserPlaceholder("%user%", IMentionable::getAsMention));
        return manager;
    }

    private static void startConsoleListener() throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            switch (scanner.nextLine()) {
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

    public static ButtonManager getButtonManager() {
        return buttonManager;
    }

    public static PlaceholderTranslator getPlaceholderTranslator() {
        return placeholderTranslator;
    }

    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

    public static Role getMuteRole() {
        return muteRole;
    }
}
