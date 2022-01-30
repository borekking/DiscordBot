package de.borekking.bot.system.ban;

import de.borekking.bot.Main;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.system.Handler;
import de.borekking.bot.system.InformationProvider;
import de.borekking.bot.system.mute.MuteSQLHandler;
import de.borekking.bot.util.discord.event.EventInformation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BanHandler implements Handler {

    /*
     * Database:
     * user:long, timestamp:long, duration:long
     *
     */

    private final Guild guild;
    private final BanSQLHandler sqlHandler;

    public BanHandler() {
        this.sqlHandler = new BanSQLHandler();
        this.guild = Main.getDiscordBot().getGuild();

        new BanChecker(this, this.sqlHandler).startChecker("Ban is over");
    }

    @Override
    public boolean use(InformationProvider provider) {
        if (!(provider instanceof BanInformationProvider)) {
            throw new IllegalArgumentException("Provider is not a valid subclass of InformationProvider!");
        }

        // Unpack
        Member member = provider.getMember();
        String reason = provider.getReason();
        long length = provider.getLength();
        int delDays = ((BanInformationProvider) provider).getDelDays();

        if (member == null) return false;

        member.ban(delDays, reason).queue(); // Discord ban
        this.sqlHandler.init(member.getId(), System.currentTimeMillis(), length); // SQL Ban
        this.sendBanInformation(member, reason); // Discord Information
        return true;
    }

    // userID: User-Tag or User-ID
    @Override
    public User undo(String userID, String reason) {
        User user = Main.getDiscordBot().getGuild().retrieveBanList().complete().stream().map(Guild.Ban::getUser)
                .filter(tempUser -> userID.equals(tempUser.getAsTag()) || userID.equals(tempUser.getId())).findAny().orElse(null);

        if (user == null) return null;

        this.sendUnbanInformation(user, reason); // Send information
        this.sqlHandler.remove(userID); // Remove SQL Entry
        this.guild.unban(user).queue(); // Actually ban
        return user;
    }

    @Override
    public boolean is(String userID) {
        // Try to find a sql entry matching given userID
        ResultSet resultSet = this.sqlHandler.get();

        try {
            while (resultSet.next()) {
                String tempUserID = resultSet.getString(MuteSQLHandler.DATABASE_MUTE_USER_COLUMN.getName());

                if (userID.equals(tempUserID)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void sendUnbanInformation(User user, String reason) {
        EventInformation information = ConfigSetting.UNBAN_PLAYER_MESSAGE.getAsEventInformation();

        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of UNBAN_PLAYER_MESSAGE (\"unbanInformation\") is null! Please check your config.");
            return;
        }

        information.apply(user, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
    }

    private void sendBanInformation(Member member, String reason) {
        // Get EventInformation from Config
        EventInformation information = ConfigSetting.BANNED_PLAYER_MESSAGE.getAsEventInformation();

        if (information == null) {
            System.err.println("Error on Banning: EventInformation value of BANNED_PLAYER_MESSAGE (\"banInformation\") is null! Please check your config.");
            return;
        }

        information.apply(member, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
    }
}
