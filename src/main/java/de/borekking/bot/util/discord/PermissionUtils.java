package de.borekking.bot.util.discord;

import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class PermissionUtils {

    private PermissionUtils() {
    }

    public static EnumSet<Permission> getPermissions(Permission... permissions) {
        return Permission.getPermissions(Permission.getRaw(permissions));
    }
}
