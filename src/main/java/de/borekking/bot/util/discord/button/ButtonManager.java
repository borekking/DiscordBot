package de.borekking.bot.util.discord.button;

import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonManager {

    public static final String BUTTON_PREFIX = "buma";

    private final ButtonIDCreator creator;
    // Member ID to List of ButtonToActionStorage
    private final Map<Long, List<ButtonToActionStorage>> buttonEvents;

    public ButtonManager() {
        this.buttonEvents = new HashMap<>();
        this.creator = new ButtonIDCreator(BUTTON_PREFIX);
    }

    public boolean containsID(long id) {
        return creator.containsID(id);
    }

    // Methode to get a unique id
    public long getNewUniqueID() {
        return this.creator.getNewUniqueID();
    }

    // Methode to apply right syntax to button´s actual ID
    public String[] getIDs(long id, int amount) {
        return this.creator.getIDs(id, amount);
    }

    public void addButtonEvent(Member member, ButtonToActionStorage e) {
        long memberID = member.getIdLong();
        if (this.buttonEvents.containsKey(memberID))
            this.buttonEvents.get(memberID).add(e);
        else
            this.buttonEvents.put(memberID, new ArrayList<>(Collections.singletonList(e)));
    }

    public ButtonToActionStorage getButtonEvent(Member member, long id) {
        List<ButtonToActionStorage> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return null;

        return list.stream().filter(buttonAction -> buttonAction.compareID(id)).findFirst().orElse(null);
    }

    public void removeButtonEvent(Member member, long id) {
        List<ButtonToActionStorage> list = this.buttonEvents.get(member.getIdLong());
        if (list == null) return;

        list.removeIf(action -> action.compareID(id));

        this.creator.remove(id);
    }

    public void clear() {
        this.buttonEvents.clear();
    }
}
