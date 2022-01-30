package de.borekking.bot.ban;

import de.borekking.bot.Main;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.discord.event.EventInformation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class BanHandler {

    /*
     * Database:
     * user:long, timestamp:long, duration:long
     *
     */

    private final Guild guild;
    private final BanSQLHandler handler;

    public BanHandler() {
        this.handler = new BanSQLHandler();
        this.guild = Main.getDiscordBot().getGuild();

        BanChecker banChecker = new BanChecker(this.handler, (state, id) -> {
            switch (state) {
                case BAN:
                    break;
                case UNBAN:
                    this.unban(id, "Ban is over.");
                    break;
            }
        });

        this.startChecker(banChecker);
    }

    public boolean ban(String userID, String reason, long length) {
        return this.ban(userID, reason, length, 0);
    }

    public boolean ban(String userID, String reason, long length, int delDays) {
        Member member = this.guild.getMemberById(userID);
        return this.ban(member, reason, length, delDays);
    }

    public boolean ban(Member member, String reason, long length) {
        return this.ban(member, reason, length, 0);
    }

    public boolean ban(Member member, String reason, long length, int delDays) {
        if (member == null) return false;
        member.ban(delDays, reason).queue(); // Discord ban
        this.handler.initBan(member.getId(), System.currentTimeMillis(), length); // SQL Ban
        this.sendBanInformation(member, reason); // Discord Information
        return true;
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

    /**
     * @param userID User-Tag or User-ID
     * @return If user could be unbanned.
     */
    public User unban(String userID, String reason) {
        User user = Main.getDiscordBot().getGuild().retrieveBanList().complete().stream().map(Guild.Ban::getUser)
                .filter(tempUser -> userID.equals(tempUser.getAsTag()) || userID.equals(tempUser.getId())).findAny().orElse(null);

        if (user == null) return null;

        this.sendUnbanInformation(user, reason); // Send information
        this.handler.removeBan(userID); // Remove SQL Entry
        this.guild.unban(user).queue(); // Actually ban
        return user;
    }

    private void sendUnbanInformation(User user, String reason) {
        EventInformation information = ConfigSetting.UNBAN_PLAYER_MESSAGE.getAsEventInformation();

        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of UNBAN_PLAYER_MESSAGE (\"unbanInformation\") is null! Please check your config.");
            return;
        }

        information.apply(user, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
    }

    private void startChecker(BanChecker banChecker) {
        new BansThread(banChecker).start();
    }

    private static class BansThread extends Thread {

        private final BanChecker banChecker;

        private BansThread(BanChecker banChecker) {
            this.banChecker = banChecker;
        }

        @Override
        public void run() {
            while (true) {
                if (Main.isReload()) return;
                this.banChecker.checkBans();
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
