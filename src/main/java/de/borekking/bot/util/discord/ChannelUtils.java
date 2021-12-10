package de.borekking.bot.util.discord;

import de.borekking.bot.Main;
import de.borekking.bot.util.java.Checker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChannelUtils {

    private static final EnumSet<Permission> voiceChannelPermissions;
    private static final EnumSet<Permission> textChannelPermission;

    static {
        voiceChannelPermissions = Permission.getPermissions(Permission.ALL_VOICE_PERMISSIONS);
        textChannelPermission = Permission.getPermissions(Permission.ALL_TEXT_PERMISSIONS);
    }

    public static boolean setTextChannelPermission(TextChannel channel, Role role, Collection<Permission> grantPermissions, Collection<Permission> denyPermission) {
        Predicate<Permission> predicate = permission -> permission != null && isTextChannelPermission(permission);

        return setChannelPermission(channel, role,
                grantPermissions.stream().filter(predicate).collect(Collectors.toList()),
                denyPermission.stream().filter(predicate).collect(Collectors.toList())
        );
    }

    public static boolean setVoiceChannelPermission(VoiceChannel channel, Role role, Collection<Permission> grantPermissions, Collection<Permission> denyPermission) {
        Predicate<Permission> predicate = permission -> permission != null && isVoiceChannelPermission(permission);

        return setChannelPermission(channel, role,
                grantPermissions.stream().filter(predicate).collect(Collectors.toList()),
                denyPermission.stream().filter(predicate).collect(Collectors.toList())
        );
    }

    private static boolean setChannelPermission(GuildChannel channel, Role role, Collection<Permission> grantPermissions, Collection<Permission> denyPermission) {
        if (!Main.getDiscordBot().getGuild().getSelfMember().hasPermission(Permission.MANAGE_PERMISSIONS)) return false;
        if (role.getGuild() != channel.getGuild()) return false;
        if (!Checker.listNonNull(grantPermissions) || !Checker.listNonNull(denyPermission)) return false;

        channel.putPermissionOverride(role).setPermissions(grantPermissions, denyPermission).queue();
        return true;
    }

    private static boolean isVoiceChannelPermission(Permission permission) {
        return voiceChannelPermissions.contains(permission);
    }

    private static boolean isTextChannelPermission(Permission permission) {
        return textChannelPermission.contains(permission);
    }
}
