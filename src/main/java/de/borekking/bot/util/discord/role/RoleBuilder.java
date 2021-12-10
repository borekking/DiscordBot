package de.borekking.bot.util.discord.role;

import de.borekking.bot.util.java.JavaUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RoleBuilder {

    private Color color;
    private boolean grouped, mentionable;
    private StringBuilder name;
    private final List<Permission> permissions;

    {
        this.permissions = new ArrayList<>();
    }

    public static RoleBuilder getFrom(String name, Color color, Permission... permissions) {
        return new RoleBuilder().name(name).color(color).permissions(permissions);
    }

    public RoleBuilder color(Color color) {
        this.color = color;
        return this;
    }

    public RoleBuilder grouped(boolean grouped) {
        this.grouped = grouped;
        return this;
    }

    public RoleBuilder mentionable(boolean mentionable) {
        this.mentionable = mentionable;
        return this;
    }

    public RoleBuilder name(String name) {
        if (name != null)
            this.name = new StringBuilder(name);
        return this;
    }

    public RoleBuilder permissions(Permission... permissions) {
        if (permissions == null) return this;
        return this.permissions(JavaUtils.getAsList(permissions));
    }

    public RoleBuilder permissions(List<Permission> permissions) {
        if (permissions != null)
            JavaUtils.addAllIf(this.permissions, permissions, perm -> perm != null);
        return this;
    }

    // Creates Role in a guild
    public Role create(Guild guild) {
        RoleAction action = guild.createRole();

        // Null means default
        if (this.name == null) {
            action.setName(null);
        } else {
            // Provide name being longer than 100
            if (this.name.length() > 100)
                this.name.setLength(100);

            action.setName(this.name.toString());
        }

        if (this.grouped)
            action.setHoisted(true);

        if (this.mentionable)
            action.setMentionable(true);

        if (this.color != null)
            action.setColor(this.color);

        // No check needed; Can't be null
        action.setPermissions(this.permissions);

        return action.complete();
    }
}
