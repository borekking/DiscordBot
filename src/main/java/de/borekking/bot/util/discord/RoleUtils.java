package de.borekking.bot.util.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RoleUtils {

    // Private constructor
    private RoleUtils() {
    }

    // Adding roles
    public static boolean addRoles(Member member, Guild guild, Collection<Role> roles) {
        if (member == null || roles == null || roles.isEmpty() || guild == null) return false;
        if (!validRoles(roles, guild)) return false;

        for (Role role : roles)
            guild.addRoleToMember(member, role).queue();

        return true;
    }

    public static boolean addRoles(Member member, Guild guild, Role... roles) {
        return addRoles(member, guild, getAsList(roles));
    }

    public static boolean addRoles(Member member, Collection<Role> roles) {
        if (member == null) return false;
        return addRoles(member, member.getGuild(), roles);
    }

    public static boolean addRoles(Member member, Role... roles) {
        return addRoles(member, getAsList(roles));
    }

    public static boolean addRoles(User user, Guild guild, Collection<Role> roles) {
        return addRoles(getMember(user, guild), roles);
    }

    public static boolean addRoles(User user, Guild guild, Role... roles) {
        return addRoles(getMember(user, guild), guild, roles);
    }

    // Safely gets Member from User
    private static Member getMember(User user, Guild guild) {
        if (user == null || guild == null) return null;
        return guild.getMember(user);
    }

    // Return List of Role from
    private static List<Role> getAsList(Role[] roles) {
        if (roles == null || roles.length == 0) return new ArrayList<>();
        return Arrays.stream(roles).collect(Collectors.toList());
    }

    // Check if roles are not null and can be used by bot
    private static boolean validRoles(Collection<Role> roles, Guild guild) {
        Member selfMember = guild.getSelfMember();
        for (Role role : roles)
            if (role == null || !selfMember.canInteract(role))
                return false;
        return true;
    }
}
