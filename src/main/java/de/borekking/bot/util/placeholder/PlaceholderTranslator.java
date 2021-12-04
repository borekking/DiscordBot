package de.borekking.bot.util.placeholder;

import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.placeholder.placeholderTypes.MemberPlaceholder;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // Methods to get this PlaceholderTranslator with extra placeholder(s)
    public PlaceholderTranslator getWith(List<GeneralPlaceholder> generalPlaceholders, List<MemberPlaceholder> memberPlaceholders) {
        PlaceholderManager<Void> manager1 = this.generalPlaceholderManager.copy();
        for (GeneralPlaceholder ph : generalPlaceholders) {
            if (ph == null) continue;
            manager1.addPlaceholder(ph);
        }

        PlaceholderManager<Member> manager2 = this.memberPlaceholderManager.copy();
        for (MemberPlaceholder ph : memberPlaceholders) {
            if (ph == null) continue;
            manager2.addPlaceholder(ph);
        }

        return new PlaceholderTranslator(manager1, manager2);
    }

    public PlaceholderTranslator getWithGeneralPHs(List<GeneralPlaceholder> generalPlaceholders) {
        return this.getWith(generalPlaceholders, new ArrayList<>());
    }

    public PlaceholderTranslator getWithMemberPHs(List<MemberPlaceholder> memberPlaceholders) {
        return this.getWith(new ArrayList<>(), memberPlaceholders);
    }

    public PlaceholderTranslator getWithPHs(GeneralPlaceholder ph0, MemberPlaceholder ph1) {
        return this.getWith(Collections.singletonList(ph0), Collections.singletonList(ph1));
    }

    public PlaceholderTranslator getWithGeneralPH(GeneralPlaceholder ph) {
        return this.getWithPHs(ph, null);
    }

    public PlaceholderTranslator getWithMemberPH(MemberPlaceholder ph) {
        return this.getWithPHs(null, ph);
    }
}
