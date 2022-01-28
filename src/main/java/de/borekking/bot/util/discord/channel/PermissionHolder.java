package de.borekking.bot.util.discord.channel;

import de.borekking.bot.util.java.JavaUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;

import java.util.ArrayList;
import java.util.List;

public class PermissionHolder {

    private final IPermissionHolder iPermissionHolder;
    private final List<Permission> denyPermissions, grantPermissions;

    public PermissionHolder(IPermissionHolder iPermissionHolder) {
        this.iPermissionHolder = iPermissionHolder;

        this.denyPermissions = new ArrayList<>();
        this.grantPermissions = new ArrayList<>();
    }

    PermissionHolder(IPermissionHolder iPermissionHolder, List<Permission> grantPermissions, List<Permission> denyPermissions) {
        this(iPermissionHolder);
        this.addGrantPermission(grantPermissions);
        this.addDenyPermission(denyPermissions);
    }

    public void addDenyPermission(List<Permission> permissions) {
        for (Permission permission : permissions)
            this.denyPermissions.add(permission);
    }

    public void addDenyPermission(Permission... permissions) {
        this.addDenyPermission(JavaUtils.getAsList(permissions));
    }

    public void addGrantPermission(List<Permission> permissions) {
        for (Permission permission : permissions)
            this.grantPermissions.add(permission);
    }

    public void addGrantPermission(Permission... permissions) {
        this.addGrantPermission(JavaUtils.getAsList(permissions));
    }

    public IPermissionHolder getiPermissionHolder() {
        return iPermissionHolder;
    }

    public List<Permission> getDenyPermissions() {
        return denyPermissions;
    }

    public List<Permission> getGrantPermissions() {
        return grantPermissions;
    }
}
