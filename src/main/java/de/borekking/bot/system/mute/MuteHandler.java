package de.borekking.bot.system.mute;

import de.borekking.bot.Main;
import de.borekking.bot.config.ConfigSetting;
import de.borekking.bot.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.system.Handler;
import de.borekking.bot.system.InformationProvider;
import de.borekking.bot.util.discord.event.EventInformation;
import de.borekking.bot.util.discord.role.RoleUtils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import org.json.simple.JSONObject;

public class MuteHandler implements Handler {

    private final Role muteRole;

    private final MuteSQLHandler sqlHandler;

    public MuteHandler(Role muteRole) {
        this.muteRole = muteRole;

        this.sqlHandler = new MuteSQLHandler();
        new MuteChecker(this, this.sqlHandler).startChecker("Mute is over");
    }

    @Override
    public boolean use(InformationProvider provider) {
        Member member = provider.getMember();

        boolean success = RoleUtils.addRoles(member, this.muteRole); // Mute by giving MuteRole
        if (!success) return false; // If adding roles didn't work return false and don't to sql and information stuff.

        this.sqlHandler.init(member.getId(), System.currentTimeMillis(), provider.getLength()); // Put in Database
        this.sendMuteInformation(member, provider.getReason()); // Send information
        return true;
    }

    @Override
    public User undo(String userID, String reason) {
        Member member = Main.getMember(userID);

        boolean removedRoles = RoleUtils.removeRoles(member, this.muteRole); // Remove MuteRole
        this.sqlHandler.remove(userID); // Remove from Database
        this.sendUnmuteInformation(member, reason); // Send information

        // Return null if Roles could NOT be removed
        return !removedRoles ? null : member.getUser();
    }

    @Override
    public boolean is(String userID) {
        Member member = Main.getMember(userID);

        return RoleUtils.hasRoles(member, this.muteRole);
    }

    private void sendMuteInformation(Member member, String reason) {
        EventInformation information = EventInformation.getFromJSON((JSONObject) ConfigSetting.MUTES.getInnerValue("muteInformation"));

        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of MUTES.muteInformation (\"mute.muteInformation\") is null! Please check your config.");
            return;
        }

        information.apply(member, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
    }

    private void sendUnmuteInformation(Member target, String reason) {
        EventInformation information = EventInformation.getFromJSON((JSONObject) ConfigSetting.MUTES.getInnerValue("unmuteInformation"));

        if (information == null) {
            System.err.println("Error on SlashCommandEvent: EventInformation value of MUTES.unmuteInformation (\"mute.unmuteInformation\") is null! Please check your config.");
            return;
        }

        information.apply(target, Main.getPlaceholderTranslator().getWithGeneralPH(new GeneralPlaceholder("%reason%", () -> reason)));
    }
}
