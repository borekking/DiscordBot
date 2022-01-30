package de.borekking.bot.system.ban;

import de.borekking.bot.system.InformationProvider;
import net.dv8tion.jda.api.entities.Member;

public class BanInformationProvider extends InformationProvider {

    private final int delDays;

    public BanInformationProvider(long length, Member member, String reason, int delDays) {
        super(length, member, reason);
        this.delDays = delDays;
    }

    public int getDelDays() {
        return delDays;
    }
}
