package de.borekking.bot.util.placeholder;

import de.borekking.bot.util.placeholder.placeholderTypes.GeneralPlaceholder;
import de.borekking.bot.util.placeholder.placeholderTypes.UserPlaceholder;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaceholderTranslator {

    private final PlaceholderManager<Void> generalPlaceholderManager;
    private final PlaceholderManager<User> memberPlaceholderManager;

    public PlaceholderTranslator(PlaceholderManager<Void> generalPlaceholderManager, PlaceholderManager<User> memberPlaceholderManager) {
        this.generalPlaceholderManager = generalPlaceholderManager;
        this.memberPlaceholderManager = memberPlaceholderManager;
    }

    public String translate(String msg, User user) {
        msg = this.translate(msg);
        return this.memberPlaceholderManager.translate(msg, user);
    }

    public String translate(String msg) {
        return this.generalPlaceholderManager.translate(msg);
    }

    // Methods to get this PlaceholderTranslator with extra placeholder(s)
    public PlaceholderTranslator getWith(List<GeneralPlaceholder> generalPlaceholders, List<UserPlaceholder> memberPlaceholders) {
        PlaceholderManager<Void> manager1 = this.generalPlaceholderManager.copy();
        for (GeneralPlaceholder ph : generalPlaceholders) {
            if (ph == null) continue;
            manager1.addPlaceholder(ph);
        }

        PlaceholderManager<User> manager2 = this.memberPlaceholderManager.copy();
        for (UserPlaceholder ph : memberPlaceholders) {
            if (ph == null) continue;
            manager2.addPlaceholder(ph);
        }

        return new PlaceholderTranslator(manager1, manager2);
    }

    public PlaceholderTranslator getWithGeneralPHs(List<GeneralPlaceholder> generalPlaceholders) {
        return this.getWith(generalPlaceholders, new ArrayList<>());
    }

    public PlaceholderTranslator getWithMemberPHs(List<UserPlaceholder> memberPlaceholders) {
        return this.getWith(new ArrayList<>(), memberPlaceholders);
    }

    public PlaceholderTranslator getWithPHs(GeneralPlaceholder ph0, UserPlaceholder ph1) {
        return this.getWith(Collections.singletonList(ph0), Collections.singletonList(ph1));
    }

    public PlaceholderTranslator getWithGeneralPH(GeneralPlaceholder ph) {
        return this.getWithPHs(ph, null);
    }

    public PlaceholderTranslator getWithMemberPH(UserPlaceholder ph) {
        return this.getWithPHs(null, ph);
    }
}
