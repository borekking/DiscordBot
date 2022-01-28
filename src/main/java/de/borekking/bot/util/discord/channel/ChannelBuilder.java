package de.borekking.bot.util.discord.channel;

import de.borekking.bot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.ArrayList;
import java.util.List;

public class ChannelBuilder {

    private ChannelType type;
    private String name, topic;
    private Category category;
    private boolean news, nsfw;
    private int position, slowmode, userlimit;
    private final List<PermissionHolder> permissions;

    public ChannelBuilder(ChannelType type, String name) {
        this.type = type;
        this.name = name;

        this.permissions = new ArrayList<>();
    }

    public ChannelBuilder type(ChannelType type) {
        this.type = type;
        return this;
    }

    public ChannelBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ChannelBuilder category(Category category) {
        this.category = category;
        return this;
    }

    public ChannelBuilder news(boolean news) {
        this.news = news;
        return this;
    }

    public ChannelBuilder nsfw(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public ChannelBuilder position(int position) {
        this.position = position;
        return this;
    }

    // Slowmode in seconds
    public ChannelBuilder slowmode(int slowmode) {
        this.slowmode = slowmode;
        return this;
    }

    public ChannelBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public ChannelBuilder userlimit(int userlimit) {
        this.userlimit = userlimit;
        return this;
    }

    public ChannelBuilder permission(PermissionHolder permissionHolder) {
        if (permissionHolder != null)
            this.permissions.add(permissionHolder);
        return this;
    }

    public ChannelBuilder setPrivate(List<Role> excludedRoles, List<Member> excludedMember) {
        // Set Private for @everyone
        PermissionHolder holder0 = new PermissionHolder(Main.getDiscordBot().getGuild().getPublicRole());
        holder0.addDenyPermission(Permission.VIEW_CHANNEL);
        this.permission(holder0);

        // Add SEE_CHANNEL permission for excludedRoles
        if (excludedRoles != null) {
            for (Role role : excludedRoles) {
                if (role == null) continue;

                PermissionHolder holder1 = new PermissionHolder(role);
                holder1.addGrantPermission(Permission.VIEW_CHANNEL);
                this.permission(holder1);
            }
        }

        // Add SEE_CHANNEL permission for excludedMember
        if (excludedMember != null) {
            for (Member member : excludedMember) {
                if (member == null) continue;

                PermissionHolder holder1 = new PermissionHolder(member);
                holder1.addGrantPermission(Permission.VIEW_CHANNEL);
                this.permission(holder1);
            }
        }

        return this;
    }

    public GuildChannel build() {
        if (!this.type.isGuild())
            throw new IllegalArgumentException();

        Guild guild = Main.getDiscordBot().getGuild();
        ChannelAction<?> action = null;

        switch (this.type) {
            case TEXT:
                action = guild.createTextChannel(this.name);
                break;
            case STAGE:
                action = guild.createStageChannel(this.name);
                break;
            case VOICE:
                action = guild.createVoiceChannel(this.name);
                break;
            case CATEGORY:
                action = guild.createCategory(this.name);
                break;
        }

        // category
        action.setParent(this.category);

        // news
        action.setNews(this.news);

        // nsfw
        action.setNSFW(this.nsfw);

        // position
        if (this.position > 0)
            action.setPosition(this.position);

        // slowmode
        if (this.slowmode <= TextChannel.MAX_SLOWMODE && this.slowmode > 0)
            action.setSlowmode(this.slowmode);

        // topic
        if (this.topic != null && !this.topic.trim().isEmpty() && this.topic.length() <= 1024)
            action.setTopic(this.topic);

        // userlimit
        if (this.type == ChannelType.VOICE && this.userlimit >= 0 && this.userlimit <= 99)
            action.setUserlimit(this.userlimit);

        // Permission
        for (PermissionHolder holder : this.permissions)
            action.addPermissionOverride(holder.getiPermissionHolder(), holder.getGrantPermissions(), holder.getDenyPermissions());

        return action.complete();
    }
}
