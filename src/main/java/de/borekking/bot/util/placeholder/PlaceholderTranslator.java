package de.borekking.bot.util.placeholder;

import net.dv8tion.jda.api.entities.Member;

public class PlaceholderTranslator {

    private final PlaceholderManager<Void> generalPlaceholderManager;
    private final PlaceholderManager<Member> memberPlaceholderManager;

    public PlaceholderTranslator(PlaceholderManager<Void> generalPlaceholderManager, PlaceholderManager<Member> memberPlaceholderManager) {
        this.generalPlaceholderManager = generalPlaceholderManager;
        this.memberPlaceholderManager = memberPlaceholderManager;
    }

    public String translate(String msg, Member member) {
        msg = this.translate(msg);
        return this.memberPlaceholderManager.translate(msg, member);
    }

    public String translate(String msg) {
        return this.generalPlaceholderManager.translate(msg);
    }

}
