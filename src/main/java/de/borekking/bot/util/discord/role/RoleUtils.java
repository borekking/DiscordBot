package de.borekking.bot.util.discord.role;

import de.borekking.bot.util.java.Checker;
import de.borekking.bot.util.java.JavaUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RoleUtils {

    // Private constructor
    private RoleUtils() {
    }

    // Modifying roles
    public static boolean modifyRoles(Member member, Guild guild, List<Role> rolesToAdd, List<Role> rolesToRemove) {
        if (member == null || guild == null || (rolesToRemove.isEmpty() && rolesToAdd.isEmpty())) return false;
        if (!guild.getSelfMember().canInteract(member)) return false;
        if (!validRoles(rolesToAdd, guild)) return false;
        if (!validRoles(rolesToRemove, guild)) return false;

        guild.modifyMemberRoles(member, rolesToAdd, rolesToRemove).queue();
        return true;
    }

    // Adding roles
    public static boolean addRoles(Member member, Guild guild, List<Role> roles) {
        return modifyRoles(member, guild, roles, Collections.EMPTY_LIST);
    }

    public static boolean addRoles(Member member, Guild guild, Role... roles) {
        return addRoles(member, guild, getAsList(roles));
    }

    public static boolean addRoles(Member member, List<Role> roles) {
        if (member == null) return false;
        return addRoles(member, member.getGuild(), roles);
    }

    public static boolean addRoles(Member member, Role... roles) {
        return addRoles(member, getAsList(roles));
    }

    public static boolean addRoles(User user, Guild guild, List<Role> roles) {
        return addRoles(getMember(user, guild), roles);
    }

    public static boolean addRoles(User user, Guild guild, Role... roles) {
        return addRoles(getMember(user, guild), guild, roles);
    }

    // Removing roles
    public static boolean removeRoles(Member member, Guild guild, List<Role> roles) {
        return modifyRoles(member, guild, Collections.EMPTY_LIST, roles);
    }

    public static boolean removeRoles(Member member, Guild guild, Role... roles) {
        return removeRoles(member, guild, getAsList(roles));
    }

    public static boolean removeRoles(Member member, List<Role> roles) {
        if (member == null) return false;
        return removeRoles(member, member.getGuild(), roles);
    }

    public static boolean removeRoles(Member member, Role... roles) {
        return removeRoles(member, getAsList(roles));
    }

    public static boolean removeRoles(User user, Guild guild, List<Role> roles) {
        return removeRoles(getMember(user, guild), roles);
    }

    public static boolean removeRoles(User user, Guild guild, Role... roles) {
        return removeRoles(getMember(user, guild), guild, roles);
    }

    // Has role
    public static boolean hasRoles(Member member, List<Role> roles) {
        if (member == null) return false;
        List<Role> membersRoles = member.getRoles();
        return membersRoles.containsAll(roles);
    }

    public static boolean hasRoles(Member member, Role... roles) {
        return hasRoles(member, getAsList(roles));
    }

    public static boolean hasRoles(User user, Guild guild, List<Role> roles) {
        return hasRoles(getMember(user, guild), roles);
    }

    public static boolean hasRoles(User user, Guild guild, Role... roles) {
        return hasRoles(getMember(user, guild), roles);
    }

    // Safely gets Member from User
    private static Member getMember(User user, Guild guild) {
        if (user == null || guild == null) return null;
        return guild.getMember(user);
    }

    // Return List of Role from
    private static List<Role> getAsList(Role[] roles) {
        return JavaUtils.getAsList(roles);
    }

    // Check if roles are not null and can be used by bot
    private static boolean validRoles(Collection<Role> roles, Guild guild) {
        // If roles are null/empty they cannot be not applied
        if (roles == null || roles.isEmpty()) return true;

        Member selfMember = guild.getSelfMember();
        return Checker.testList(roles, role -> role == null || !selfMember.canInteract(role));
    }
}
