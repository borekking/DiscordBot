package de.borekking.bot.system;

import net.dv8tion.jda.api.entities.Member;

public class InformationProvider {

    /*
     * Interface for marking classes which are used to
     * store information about bans and mutes.
     *
     */

    private final long length;

    private final Member member;

    private final String reason;

    public InformationProvider(long length, Member member, String reason) {
        this.length = length;
        this.member = member;
        this.reason = reason;
    }

    public long getLength() {
        return length;
    }

    public Member getMember() {
        return member;
    }

    public String getReason() {
        return reason;
    }
}
