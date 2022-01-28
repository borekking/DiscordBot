package de.borekking.bot;

import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.config.ConfigurationManager;
import de.borekking.bot.sql.MySQLClient;
import de.borekking.bot.sql.SQLTable;
import de.borekking.bot.ticket.TicketManager;
import de.borekking.bot.ticket.TicketType;
import de.borekking.bot.util.discord.Timestamp;
import de.borekking.bot.util.discord.button.ButtonManager;
import de.borekking.bot.placeholder.PlaceholderManager;
import de.borekking.bot.placeholder.PlaceholderTranslator;
import de.borekking.bot.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.placeholder.placeholderTypes.UserPlaceholder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static TicketManager ticketManager;
    private static MySQLClient mySQLClient;

    public static void main(String[] args) throws IOException, ParseException {
        configurationManager = new ConfigurationManager("config.json");
        buttonManager = new ButtonManager();

        load();

        mySQLClient = createMySQLClient();
        SQLTable.loadTables();

        try {
            ticketManager = new TicketManager();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addTicketTypes();

        placeholderTranslator = new PlaceholderTranslator(getGeneralPlaceholderManager(), getMemberPlaceholderManager());

        startConsoleListener();
    }

    public static void reload() throws IOException, ParseException {
        ticketManager.clear();
        discordBot.disableBot();
        buttonManager.clear();
        load();
        addTicketTypes();
    }

    public static void exit() {
        if (discordBot != null)
            discordBot.disableBot();
        System.exit(0);
    }

    private static MySQLClient createMySQLClient() {
        ConfigSetting sql = ConfigSetting.SQL;
        String host = (String) sql.getInnerValue("host");
        String database = (String) sql.getInnerValue("database");
        String user = (String) sql.getInnerValue("user");
        String password = (String) sql.getInnerValue("password");
        return new MySQLClient(host, database, user, password);
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

    private static void addTicketTypes() {
        try {
            ResultSet resultSet = mySQLClient.getQuery("SELECT * FROM " + SQLTable.TICKET_TYPE_TABLE.getName() + ";");

            while (resultSet.next()) {
                String identifier = resultSet.getString(TicketType.DATABASE_IDENTIFIER_COLUMN.getName());
                String buttonID = resultSet.getString(TicketType.DATABASE_BUTTON_ID_COLUMN.getName());
                ticketManager.loadTicketType(identifier, buttonID);
            }
        } catch (SQLException e) {
            System.err.println("Database Error!");
            Main.exit();
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

    public static TicketManager getTicketManager() {
        return ticketManager;
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

    public static MySQLClient getMySQLClient() {
        return mySQLClient;
    }
}
